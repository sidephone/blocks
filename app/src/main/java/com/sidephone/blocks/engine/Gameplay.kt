package com.sidephone.blocks.engine

import android.util.Log
import android.view.KeyEvent
import androidx.annotation.AnyThread
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import com.sidephone.blocks.engine.entities.BlockHeap
import com.sidephone.blocks.engine.entities.Playground
import com.sidephone.blocks.engine.entities.pieces.Piece
import com.sidephone.blocks.engine.entities.PieceBag
import com.sidephone.blocks.engine.entities.pieces.PieceI
import com.sidephone.blocks.engine.graphics.DrawCommandGroup
import com.sidephone.blocks.engine.graphics.GameFrame
import com.sidephone.blocks.settings.Settings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit


/**
 * The main game engine class. It contains the game loop, input handling, and game state management.
 * It is designed to be simple and easy to understand, so you can modify it to create your own game.
 */
class Gameplay {
	companion object {
		private val LOG_TAG = Gameplay::class.java.simpleName
	}

	// game loop
	private var executor = Executors.newSingleThreadScheduledExecutor()
	private var engineLooper: Future<*>? = null
	private var isPaused = false

	// input
	@Volatile private var pressedKeys = setOf<Int>()

	var fallFasterPressed = false
	var leftPressed = false
	var rightPressed = false
	var turnClockwisePressed = false
	var turnCounterClockwisePressed = false

	// output
	private var onStartButtonPressed = {}
	private var onStarted = {}

	private val _lines = MutableStateFlow(0)
	val lines: StateFlow<Int> = _lines

	private val _level = MutableStateFlow(0)
	val level: StateFlow<Int> = _level

	private val _score = MutableStateFlow(0)
	val score: StateFlow<Int> = _score


	// graphics
	@Volatile private var viewportWidth = 1f
	@Volatile private var viewportHeight = 1f
	@Volatile var currentFrame: GameFrame = GameFrame()
	@Volatile private var firstIteration = true

	// game objects
	private var blockHeap = BlockHeap()
	private var piece: Piece = PieceI()
	private var pieceBag = PieceBag()
	private var playground = Playground()


	init {
	    reset()
	}


	@MainThread fun scoreboardPosition() = playground.scoreboardPosition()
	@MainThread fun scoreboardWidth() = playground.scoreboardWidth()


	/**
	 * Set the initial state of the game. Call this whenever you need to restart the game.
	 */
	@MainThread
	fun reset() {
		pressedKeys = setOf()

		_lines.value = 0
		_level.value = 0
		_score.value = 0

		fallFasterPressed = false
		leftPressed = false
		rightPressed = false
		turnClockwisePressed = false
		turnCounterClockwisePressed = false


		playground.create(viewportWidth)
		blockHeap.clear()
		piece = pieceBag.pop()
		piece.spawn(System.currentTimeMillis(), playground.position(), playground.dimensions(), playground.cellSize())

		if (!isGameThreadAlive()) {
			if (!executor.isShutdown && !executor.isTerminated) {
				executor.shutdownNow()
			}
			executor = Executors.newSingleThreadScheduledExecutor()
		}
	}


	/**
	 * Handle the pressed keys for your game logic.
	 * For each key you can call appropriate handler. E.g. if KeyEvent.KEYCODE_DPAD_UP, call
	 * "moveUp()" function, or if KeyEvent.KEYCODE_BUTTON_A, call "jump()" function. You can also
	 * choose to ignore some keys if you don't need them for your game.
	 * When a key is released, you will receive a new list of pressed keys without that key.
	 *
	 * @param keys The set of currently pressed keys represented by their KeyEvent key codes.
	 */
	@MainThread
	fun onPressedKeys(keys: Set<Int>) {
		pressedKeys = keys.toSet() // make a copy for thread safety
		preprocessInput()
	}


	/**
	 * Adjust the dimension of the game scene. All rendering will be performed using these.
	 */
	@AnyThread
	fun setViewportSize(width: Int, height: Int) {
		if (width <= 0 || height <= 0) {
			Log.w(LOG_TAG, "Ignoring invalid viewport size: width=$width, height=$height. Must be positive.")
			return
		}

		viewportWidth = width.toFloat()
		viewportHeight = height.toFloat()
	}


	/**
	 * Start or resume the game loop, or if already running, do nothing.
	 */
	@MainThread
	fun start() {
		if (isGameThreadAlive()) {
			return
		}

		isPaused = false
		firstIteration = true
		pressedKeys = emptySet()

		engineLooper = executor.scheduleWithFixedDelay(
			{ advance() },
			0,
			1_000_000_000L / Settings.Gameplay.TARGET_IPS,
			TimeUnit.NANOSECONDS
		)

		onStarted()

		Log.d(LOG_TAG, "Gameplay loop started at ${Settings.Gameplay.TARGET_IPS} iterations per second")
	}


	/**
	 * Pause the game loop, or if already paused, do nothing.
	 */
	@MainThread
	fun pause() {
		if (isPaused) {
			return
		}

		engineLooper?.cancel(true)
		isPaused = true

		Log.d(LOG_TAG, "Gameplay loop paused")
	}


	/**
	 * Stop the game loop and release resources. After calling this, you can not resume the game
	 * anymore, you can only use "reset()" to start a new game.
	 */
	@MainThread
	fun stop() {
		isPaused = false
		executor.shutdownNow()
		engineLooper?.cancel(true)
		engineLooper = null
		Log.d(LOG_TAG, "Gameplay loop stopped")
	}


	/**
	 * A utility function that returns true if the game loop is currently running.
	 */
	@MainThread
	fun isRunning(): Boolean {
		return !isPaused && isGameThreadAlive()
	}


	/**
	 * A utility function that returns true if the game loop is currently paused.
	 */
	@MainThread
	fun isPaused(): Boolean {
		return isPaused
	}


	/**
	 * Handle the "Start" button press. Pauses the game (if running) and notifies listeners (e.g. UI)
	 * so they can navigate back to the main menu or perform other actions.
	 */
	@MainThread
	fun onStartButton() {
		pause()
		onStartButtonPressed()
	}


	/**
	 * Set an optional callback to be invoked when the game is paused. This can be used to navigate
	 * back to the main menu or perform other actions.
	 */
	@MainThread
	fun setOnStartButtonPressedCallback(callback: () -> Unit): Gameplay {
		onStartButtonPressed = callback
		return this
	}


	/**
	 * Set an optional callback to be invoked immediately before the game starts.
	 */
	@MainThread
	fun setOnStartedCallback(callback: () -> Unit): Gameplay {
		onStarted = callback
		return this
	}


	/**
	 * Returns true when the game thread executor is still working.
	 */
	@MainThread
	private fun isGameThreadAlive(): Boolean {
		return !executor.isShutdown && !executor.isTerminated && (engineLooper?.isDone == false)
	}


	/**
	 * The main game loop function. This is equivalent to a single step or "frame" in the game. It
	 * is called repeatedly at a fixed interval to read the input, update state and perform other game
	 * logic. Finally, the "render()" method draws the current state to the screen.
	 */
	@WorkerThread
	private fun advance() {
		try {
			val now = System.currentTimeMillis()
			processGameInput()
			runLogic(now)
			render()
		} catch (e: Exception) {
			Log.e(LOG_TAG, "Failed advancing ahead gameplay. ${e.message}", e)
		}
	}


	/**
	 * Perform any non-game related actions, immediately after receiving the pressed keys. For example,
	 * pause the game, when "KeyEvent.KEYCODE_BUTTON_START" is pressed.
	 */
	@MainThread
	private fun preprocessInput() {
		if (KeyEvent.KEYCODE_BUTTON_START in pressedKeys) {
			onStartButton()
		}
	}


	/**
	 * For each keypress, calls the appropriate game logic function exactly once.
	 */
	@WorkerThread
	private fun processGameInput() {
		val keys = pressedKeys.toSet() // make a copy for thread safety

		val turnClockwise = (KeyEvent.KEYCODE_BUTTON_B in keys || KeyEvent.KEYCODE_DPAD_UP in keys)
		if (turnClockwise && !turnClockwisePressed) {
			turnClockwisePressed = true
			piece.rotateClockwise()
		} else if (!turnClockwise) {
			turnClockwisePressed = false
		}

		val turnCounterClockwise = KeyEvent.KEYCODE_BUTTON_A in keys
		if (turnCounterClockwise && !turnCounterClockwisePressed) {
			turnCounterClockwisePressed = true
			piece.rotateCounterClockwise()
		} else if (!turnCounterClockwise) {
			turnCounterClockwisePressed = false
		}

		val fallFaster = KeyEvent.KEYCODE_DPAD_DOWN in keys
		if (fallFaster && !fallFasterPressed) {
			fallFasterPressed = true
			piece.moveDown(blockHeap.getBlocks())
		} else if (!fallFaster) {
			fallFasterPressed = false
		}

		val left = KeyEvent.KEYCODE_DPAD_LEFT in keys
		if (left && !leftPressed) {
			leftPressed = true
			piece.moveLeft()
		} else if (!left) {
			leftPressed = false
		}

		val right = KeyEvent.KEYCODE_DPAD_RIGHT in keys
		if (right && !rightPressed) {
			rightPressed = true
			piece.moveRight()
		} else if (!right) {
			rightPressed = false
		}
	}


	@WorkerThread
	private fun render() {
		val screenObjects = mutableListOf<DrawCommandGroup>()
		screenObjects.add(playground.draw())
		screenObjects.add(blockHeap.draw(playground.position()))
		screenObjects.add(piece.draw())

		currentFrame = GameFrame(Playground.BACKGROUND, screenObjects)
	}


	@WorkerThread
	private fun runLogic(now: Long) {
		piece.fall(now, level.value, blockHeap.getBlocks())
		if (piece.isAtTheBottom()) {
			blockHeap.add(piece)
			piece = pieceBag.pop()
			piece.spawn(now, playground.position(), playground.dimensions(), playground.cellSize())
		}
	}
}
