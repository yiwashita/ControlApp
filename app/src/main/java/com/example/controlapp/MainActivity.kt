package com.example.controlapp

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothProfile
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private val bluetoothAdapter: BluetoothAdapter? by lazy { BluetoothAdapter.getDefaultAdapter() }

    // フラグメントの初期化を行う
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

                // Bluetoothが使用中か判定
                if (isBluetoothConnected()) {

                    println("Bluetoothが接続中でした")

                } else {

                    println("Bluetoothが未使用でした、OFFにする処理を実行")

                    // BluetoothをOFFにする処理
                    disableBluetooth()
                }
            }
        }
    }

    // Bluetoothが使用中か判定する関数
    private fun isBluetoothConnected(): Boolean {

        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        // Bluetoothが無効か、利用できない場合
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled) {

            // 接続されていないとみなす
            return false
        }

        val connectedDevices = bluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEADSET)
        return connectedDevices == BluetoothProfile.STATE_CONNECTED
    }

    // Bluetoothを無効化する
    private fun disableBluetooth() {

        // Bluetoothが有効になっているか判定
        if (bluetoothAdapter?.isEnabled == true) {

            // Bluetoothを無効化
            bluetoothAdapter!!.disable()

            Toast.makeText(this, "Bluetoothを無効化しました！", Toast.LENGTH_SHORT).show()

        } else {

            Toast.makeText(this, "Bluetoothは既に無効化されていますね。。", Toast.LENGTH_SHORT).show()
        }
    }


    // 権限リクエストを開始
    private fun requestBluetoothPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.BLUETOOTH_CONNECT)) {

            Toast.makeText(this, "Bluetoothを制御するには権限が必要かもです", Toast.LENGTH_SHORT).show()
        }

        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH_CONNECT), BLUETOOTH_PERMISSION_REQUEST_CODE)

    }

    //権限をリクエストする？
    override fun onRequestPermissionsResult (requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == BLUETOOTH_PERMISSION_REQUEST_CODE) {

            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                disableBluetooth()
            } else {

                Toast.makeText(this, "権限が拒否されちゃいました...", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // クラス内に作成されるSingleton(１つだけしか持ちたくないもの)
    // 一度だけインスタンス化でき、グローバルにアクセスできるようなクラス
    companion object {
        private const val BLUETOOTH_PERMISSION_REQUEST_CODE = 1
    }

}