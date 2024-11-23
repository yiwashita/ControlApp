package com.example.controlapp

import android.bluetooth.BluetoothAdapter
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest

class MainActivity : AppCompatActivity() {

    private val bluetoothAdapter: BluetoothAdapter? by lazy { BluetoothAdapter.getDefaultAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bluetoothButton: Button = findViewById(R.id.offButton)

        // ボタンを押したときの処理
        bluetoothButton.setOnClickListener {

            // 必要な権限をリクエスト
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

                // 権限をリクエスト
                requestBluetoothPermission()

            } else {

                disableBluetooth()
            }
        }
    }

    // Bluetoothを無効化する
    private fun disableBluetooth() {

        if (bluetoothAdapter?.isEnabled == true) {

            bluetoothAdapter!!.disable()

            Toast.makeText(this, "Bluetoothを無効化しました", Toast.LENGTH_SHORT).show()

        } else {

            Toast.makeText(this, "Bluetoothは既に無効化されています", Toast.LENGTH_SHORT).show()
        }
    }

    // 権限リクエストを開始
    private fun requestBluetoothPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.BLUETOOTH_CONNECT)) {

            Toast.makeText(this, "Bluetoothを制御するには権限が必要です", Toast.LENGTH_SHORT).show()
        }

        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH_CONNECT), BLUETOOTH_PERMISSION_REQUEST_CODE)

    }

    override fun onRequestPermissionsResult( requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == BLUETOOTH_PERMISSION_REQUEST_CODE) {

            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                disableBluetooth()
            } else {
                Toast.makeText(this, "権限が拒否されました", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val BLUETOOTH_PERMISSION_REQUEST_CODE = 1
    }

}