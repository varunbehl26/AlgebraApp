
package varunbehl.algebra.util

import android.os.Handler
import android.os.Message
import android.os.SystemClock

/**
 * Schedule a countdown until a time in the future, with
 * regular notifications on intervals along the way.
 *
 * Example of showing a 30 second countdown in a text field:
 *
 * <pre class="prettyprint">
 * new CountdownTimer(30000, 1000) {
 *
 * public void onTick(long millisUntilFinished) {
 * mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
 * }
 *
 * public void onFinish() {
 * mTextField.setText("done!");
 * }
 * }.start();
</pre> *
 *
 * The calls to [.onTick] are synchronized to this object so that
 * one call to [.onTick] won't ever occur before the previous
 * callback is complete.  This is only relevant when the implementation of
 * [.onTick] takes an amount of time to execute that is significant
 * compared to the countdown interval.
 */
abstract class CountDownTimer
/**
 * @param millisInFuture The number of millis in the future from the call
 * to [.start] until the countdown is done and [.onFinish]
 * is called.
 * @param countDownInterval The interval along the way to receive
 * [.onTick] callbacks.
 */
(
        /**
         * Millis since epoch when alarm should stop.
         */
        private val mMillisInFuture: Long,
        /**
         * The interval in millis that the user receives callbacks
         */
        private val mCountdownInterval: Long) {

    private var mStopTimeInFuture: Long = 0

    private var mPauseTime: Long = 0

    private var mCancelled = false

    private var mPaused = false


    // handles counting down
    private val mHandler = object : Handler() {

        override fun handleMessage(msg: Message) {

            synchronized(this@CountDownTimer) {
                if (!mPaused) {
                    val millisLeft = mStopTimeInFuture - SystemClock.elapsedRealtime()

                    if (millisLeft <= 0) {
                        onFinish()
                    } else if (millisLeft < mCountdownInterval) {
                        // no tick, just delay until done
                        sendMessageDelayed(obtainMessage(MSG), millisLeft)
                    } else {
                        val lastTickStart = SystemClock.elapsedRealtime()
                        onTick(millisLeft)

                        // take into account user's onTick taking time to execute
                        var delay = lastTickStart + mCountdownInterval - SystemClock.elapsedRealtime()

                        // special case: user's onTick took more than interval to
                        // complete, skip to next interval
                        while (delay < 0) delay += mCountdownInterval

                        if (!mCancelled) {
                            sendMessageDelayed(obtainMessage(MSG), delay)
                        }
                    }
                }
            }
        }
    }

    /**
     * Cancel the countdown.
     *
     * Do not call it from inside CountDownTimer threads
     */
    fun cancel() {
        mHandler.removeMessages(MSG)
        mCancelled = true
    }

    /**
     * Start the countdown.
     */
    @Synchronized
    fun start() {
        if (mMillisInFuture <= 0) {
            onFinish()
            return
        }
        mStopTimeInFuture = SystemClock.elapsedRealtime() + mMillisInFuture
        mHandler.sendMessage(mHandler.obtainMessage(MSG))
        mCancelled = false
        mPaused = false
    }

    /**
     * Pause the countdown.
     */
    fun pause() {
        mPauseTime = mStopTimeInFuture - SystemClock.elapsedRealtime()
        mPaused = true
        // return mPauseTime;
    }

    /**
     * Resume the countdown.
     */
    fun resume() {
        mStopTimeInFuture = mPauseTime + SystemClock.elapsedRealtime()
        mPaused = false
        mHandler.sendMessage(mHandler.obtainMessage(MSG))
        // return mPauseTime;
    }

    /**
     * Callback fired on regular interval.
     * @param millisUntilFinished The amount of time until finished.
     */
    abstract fun onTick(millisUntilFinished: Long)

    /**
     * Callback fired when the time is up.
     */
    abstract fun onFinish()

    companion object {


        private val MSG = 1
    }
}
