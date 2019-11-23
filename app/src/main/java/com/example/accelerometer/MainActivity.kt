package com.example.accelerometer

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

// Interface SensorEventListener służy do otrzymywania powiadomień z SensorManagera,
// gdy pojawią sie nowe dane z czujnika.
// Interface posiada dwie abstarkcyjne metody:
// abstract fun onAccuracyChanged(sensor: Sensor!, accuracy: Int): Unit
// abstract fun onSensorChanged(event: SensorEvent!): Unit
class MainActivity : AppCompatActivity(), SensorEventListener{

    // Abstrakcyjna klasa SensorMenager umożliwia dostęp do czujników w urządzeniu
    lateinit var sensorManager: SensorManager
    // Klasa Sensor reprezentuje czujnik
    lateinit var sensorAccelerometer : Sensor

    var maxXYZ = arrayOf(0.toFloat(), 0.toFloat(), 0.toFloat())

    // Metoda onAccuracyChanged wywoływana w przypadku zmiany dokładności czujnika
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    // Metoda onSensorChanged wywoływana przy każdym zdarzeniu z czujnika (nowym odczycie parametrów)
    override fun onSensorChanged(event: SensorEvent?) {

        // Wydobywanie danych z czujnika za pomocą event.values
        accelerometer_data.text =
                "x: ${event!!.values[0]}\n" +
                "y: ${event.values[1]}\n" +
                "z: ${event.values[2]}"

        // Zapisywanie największych wartości
        for (i in 0..2 ){
            if(event!!.values[i] < 0){
                if(-event!!.values[i] > maxXYZ[i]){
                    maxXYZ[i] = -event!!.values[i]
                }
            }
            else{
                if(event!!.values[i] > maxXYZ[i]){
                    maxXYZ[i] = event!!.values[i]
                }
            }
        }

        accelerometer_max_data.text =
                    "max x: ${maxXYZ[0]}\n" +
                    "max y: ${maxXYZ[1]}\n" +
                    "max z: ${maxXYZ[2]}"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Metoda getSystemServvice zwraca uchwyt do usługi określonej parametrem Context.SENSOR_SERVICE
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        // Metoda getDefaultSensor zwraca domyślny czujnik określonego typu
        sensorAccelerometer =  sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        // Metoda registerListener rejestruje SensorListenera dla danego czujnika
        sensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    // W celu zaoszczędzenia baterii, należy wyłączyć zczytywanie danych z sensorów, gdy aplikacja jest zawieszona

    override fun onPause() {
        super.onPause()
        // Metoda unregisterListener zaprzestaje rejestrowania SensorListenera
        sensorManager.unregisterListener(this, sensorAccelerometer)
    }

    // Gdy aplikacja zostaje wznowiona, trzeba na nowo włączyć zczytywanie danych z czujnika
    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }
}
