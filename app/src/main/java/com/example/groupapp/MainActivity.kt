package com.example.groupapp

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    lateinit var edtName: EditText
    lateinit var edtNumber: EditText
    lateinit var edtNameResult: EditText
    lateinit var edtNumberResult: EditText

    lateinit var btnInit: Button
    lateinit var btnInsert: Button
    lateinit var btnSelect: Button
    lateinit var btnUpdate: Button
    lateinit var btnDelete: Button

    lateinit var myHelper: myDBHelper
    lateinit var sqlDB: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edtName = findViewById(R.id.edtName)
        edtNumber = findViewById(R.id.edtNumber)
        edtNameResult = findViewById(R.id.edtNameResult)
        edtNumberResult = findViewById(R.id.edtNumberResult)

        btnInit = findViewById(R.id.btnInit)
        btnInsert = findViewById(R.id.btnInsert)
        btnSelect = findViewById(R.id.btnSelect)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)

        myHelper = myDBHelper(this)

        //초기화 버튼을 눌렀을 때
        btnInit.setOnClickListener {
            sqlDB = myHelper.writableDatabase

            //테이블 갱신
            myHelper.onUpgrade(sqlDB, 1,2)

            Toast.makeText(applicationContext,"초기화",Toast.LENGTH_SHORT).show()
            sqlDB.close()
        }

        //삽입 버튼을 눌렀을 때
        btnInsert.setOnClickListener {
            sqlDB = myHelper.writableDatabase

            sqlDB.execSQL("INSERT INTO groupTBL VALUES ('"+edtName.text.toString()+"', "
                    +edtNumber.text.toString() + ");")

            sqlDB.close()

            Toast.makeText(applicationContext,"입력됨",Toast.LENGTH_SHORT).show()
            btnSelect.callOnClick() //조회 이벤트 강제 발생
        }

        //조회를 클릭했을 때
        btnSelect.setOnClickListener {
            sqlDB = myHelper.readableDatabase

            var cursor: Cursor
            cursor = sqlDB.rawQuery("SELECT * FROM groupTBL;" , null)

            var strNames = "그룹 이름" + "\r\n" + "---------------" + "\r\n"
            var strNumbers = "인원" + "\r\n" + "-------" + "\r\n"

            while (cursor.moveToNext()){
                strNames += cursor.getString(0) + "\r\n"
                strNumbers += cursor.getString(1) + "\r\n"
            }

            edtNameResult.setText(strNames)
            edtNumberResult.setText(strNumbers)

            cursor.close()

            Toast.makeText(applicationContext,"조회됨",Toast.LENGTH_SHORT).show()
            sqlDB.close()
        }

        // 수정 버튼을 눌렀을 때
        btnUpdate.setOnClickListener {
            sqlDB = myHelper.writableDatabase

            sqlDB.execSQL("UPDATE groupTBL SET gNumber = " + edtNumber.text + " WHERE gName = '"
            + edtName.text.toString() + "';")

            sqlDB.close()

            Toast.makeText(applicationContext,"수정됨",Toast.LENGTH_SHORT).show()
            btnSelect.callOnClick() //조회 이벤트 강제 발생
        }

        //삭제 버튼을 눌렀을 때
        btnDelete.setOnClickListener {
            sqlDB = myHelper.writableDatabase

            sqlDB.execSQL("DELETE FROM groupTBL WHERE gName = '" + edtName.text.toString() + "';")

            sqlDB.close()

            Toast.makeText(applicationContext,"삭제됨",Toast.LENGTH_SHORT).show()
            btnSelect.callOnClick() //조회 이벤트 강제 발생
        }
    }

    inner class myDBHelper(context: Context) : SQLiteOpenHelper(context,"groupDB",null,1){

        override fun onCreate(db: SQLiteDatabase?){
            db!!.execSQL("CREATE TABLE groupTBL(gName CHAR(20) PRIMARY KEY, gNumber Integer);")
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db!!.execSQL("DROP TABLE IF EXISTS groupTBL")
            onCreate(db)
        }
    }
}