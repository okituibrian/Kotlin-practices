package com.example.mysimplecalculator


import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.lang.ArithmeticException

class MainActivity : AppCompatActivity() {
    private lateinit var display: TextView
    private var lastNumeric: Boolean = false
    private var lastDot: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        display = findViewById(R.id.display)
    }

    fun onDigit(view: View) {
        val digit = (view as TextView).text.toString()
        if (display.text.toString() == "0") {
            display.text = digit
        } else {
            display.append(digit)
        }
        lastNumeric = true
    }


    fun onClear(view: View) {
        if (display.text.isNotEmpty()) {
            display.text = display.text.substring(0, display.text.length - 1)
            val newText = display.text.toString()
            lastNumeric = newText.isNotEmpty() && newText.last().isDigit()
            lastDot = newText.contains('.')
        }
    }


    fun allClear(view: View) {
        display.text = "0"
        lastNumeric = false
        lastDot = false
    }

    fun onDecimalPoint(view: View) {
        if (lastNumeric && !lastDot) {
            display.append(".")
            lastNumeric = false
            lastDot = true
        }
    }

    fun onOperator(view: View) {
        if (lastNumeric && !isOperatorAdded(display.text.toString())) {
            display.append((view as TextView).text)
            lastNumeric = false
            lastDot = false
        }
    }

    fun onEqual(view: View) {
        if (lastNumeric) {
            var value = display.text.toString()
            var prefix = ""

            try {
                if (value.startsWith("-")) {
                    prefix = "-"
                    value = value.substring(1)
                }

                var result: String? = null

                if (value.contains("-")) {
                    val splitValue = value.split("-")
                    var one = splitValue[0]
                    var two = splitValue[1]

                    if (prefix.isNotEmpty()) {
                        one = prefix + one
                    }

                    result = removeZeroAfterDot((one.toDouble() - two.toDouble()).toString())
                } else if (value.contains("+")) {
                    val splitValue = value.split("+")
                    var one = splitValue[0]
                    var two = splitValue[1]

                    if (prefix.isNotEmpty()) {
                        one = prefix + one
                    }

                    result = removeZeroAfterDot((one.toDouble() + two.toDouble()).toString())
                } else if (value.contains("x")) {
                    val splitValue = value.split("x")
                    var one = splitValue[0]
                    var two = splitValue[1]

                    if (prefix.isNotEmpty()) {
                        one = prefix + one
                    }

                    result = removeZeroAfterDot((one.toDouble() * two.toDouble()).toString())
                } else if (value.contains("÷")) {
                    val splitValue = value.split("÷")
                    var one = splitValue[0]
                    var two = splitValue[1]

                    if (prefix.isNotEmpty()) {
                        one = prefix + one
                    }

                    result = removeZeroAfterDot((one.toDouble() / two.toDouble()).toString())
                }

                if (result != null) {
                    display.append("=$result")
                }
            } catch (e: ArithmeticException) {
                e.printStackTrace()
            }
        }
    }


    private fun removeZeroAfterDot(result: String): String {
        var value = result
        if (result.contains(".0"))
            value = result.substring(0, result.length - 2)
        return value
    }

    private fun isOperatorAdded(value: String): Boolean {
        return if (value.startsWith("-")) {
            false
        } else {
            value.contains("÷") || value.contains("*") || value.contains("+") || value.contains("-")
        }
    }
}
