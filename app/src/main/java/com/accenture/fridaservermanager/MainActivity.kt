package com.accenture.fridaservermanager

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.accenture.fridaservermanager.databinding.ActivityMainBinding
import java.io.File
import java.io.IOException
import java.nio.file.attribute.PosixFilePermissions
import kotlin.io.path.Path
import kotlin.io.path.setPosixFilePermissions


enum class SERVER_STATE {
    IDLE,
    RUNNING
}

class CustomArrayAdapter(context: Context, resource: Int, objects: List<String>) :
    ArrayAdapter<String>(context, resource, objects) {

    // Override isEnabled to disable specific items
    override fun isEnabled(position: Int): Boolean {
        return position != 0
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        val tv = view as TextView
        if (position == 0) {
            tv.setTextColor(Color.GRAY)
        } else {
            tv.setTextColor(Color.BLACK)
        }
        return view
    }
}

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var arch: String
    private var serverState = SERVER_STATE.IDLE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("arch", System.getProperty("os.arch")!!.toString())
        try {
            arch = if (System.getProperty("os.arch")!!.toString().contains("x86"))
                "x86"
            else if (
                System.getProperty("os.arch")!!.toString().contains("arm") ||
                System.getProperty("os.arch")!!.toString().contains("aarch64"))
                "arm64"
            else
                ""
        } catch (e: Exception) {
            arch = ""
            Toast.makeText(this, "Failed to get architecture info", Toast.LENGTH_SHORT).show()
        }

        val fridaServerFiles = getFridaServerFiles()

        val adapter = CustomArrayAdapter(
            this,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            fridaServerFiles
        )
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter

        binding.rootRightsTip.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("How to grant root rights?")
                .setMessage("Go to the Magisk app, switch to the \"Superuser\" tab and toggle the switch for this app.")
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
            val dialog = builder.create()
            dialog.show()
        }

        updateStartButtonState()
    }

    private fun updateStartButtonState() {

        val pid = getCurrentPID()
        serverState = if (pid == "")
            SERVER_STATE.IDLE
        else
            SERVER_STATE.RUNNING

        when(serverState) {
            SERVER_STATE.IDLE -> {
                binding.fridaRunning.visibility = View.INVISIBLE
                binding.startButton.text = getString(R.string.start)
                binding.startButton.setOnClickListener {
                    if(binding.spinner.selectedItemPosition != 0)
                        startServer()
                    else
                        Toast.makeText(this, "No server file selected", Toast.LENGTH_SHORT).show()
                }
            }
            SERVER_STATE.RUNNING -> {
                Toast.makeText(this, "Server is running", Toast.LENGTH_SHORT).show()
                binding.fridaRunning.visibility = View.VISIBLE
                binding.fridaRunning.text = getString(R.string.frida_running_label, pid)
                binding.startButton.text = getString(R.string.stop)
                binding.startButton.setOnClickListener {
                    stopServer()
                }
            }
        }
    }

    private fun getFridaServerFiles(): List<String> {

        val fridaVersions = mutableListOf(getString(R.string.select_frida_version))
        try {
            val fileNames = assets.list("frida_server_versions/$arch/")
            fileNames?.forEach { fileName ->
                fridaVersions.add("Version " + fileName.substring(fileName.indexOfFirst { it.isDigit() }))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return fridaVersions
    }

    private fun startServer() {
        try {
            val executableName = "frida-server-" + binding.spinner.selectedItem.toString().split(" ")[1]
            val executableFile = File(filesDir, executableName)

            if (!executableFile.exists()) {
                executableFile.createNewFile()
                Path(executableFile.absolutePath.toString()).setPosixFilePermissions(PosixFilePermissions.fromString("rwxrwxrwx"))
                copyExecutableFromAssets(executableName, executableFile)
            }

            val command = "su -c ${executableFile.absolutePath}"
            Runtime.getRuntime().exec(command)

            if(getCurrentPID() == "")
                Toast.makeText(this, "Could not start server. Check compatibility and root rights.", Toast.LENGTH_SHORT).show()

            updateStartButtonState()

        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to start server. Phone must be rooted in order to use FridaRunner.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun stopServer() {
        val pid = getCurrentPID()
        if (pid == "") {
            Toast.makeText(this, "Server process not found", Toast.LENGTH_SHORT).show()
        } else {
            val command = "su -c kill -9 $pid"
            Runtime.getRuntime().exec(command)

            updateStartButtonState()
        }
    }

    private fun copyExecutableFromAssets(assetName: String, outputFile: File) {
        assets.open("frida_server_versions/$arch/$assetName").use { input ->
            outputFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
    }

    private fun getCurrentPID(): String {
        try {
            val command = "su -c ps -A | grep frida-server"
            val process = Runtime.getRuntime().exec(command)
            process.waitFor()
            try {
                val output =
                    process.inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() }
                        .split("\\s+".toRegex())
                return if (output.size > 1)
                    output[1]
                else
                    ""
            } catch (e: Exception) {
                e.printStackTrace()
                return ""
            }
        } catch (e: IOException) {
            Toast.makeText(this, "su not found. Phone must be rooted in order to use FridaRunner.", Toast.LENGTH_SHORT).show()
            return ""
        }
    }
}