package com.example.goodhouse;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    // 파이어베이스 데이터베이스 연동
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    HashMap<Integer, Integer> noise = new HashMap<>();

    //DatabaseReference는 데이터베이스의 특정 위치로 연결하는 거라고 생각하면 된다.
    //현재 연결은 데이터베이스에만 딱 연결해놓고
    //키값(테이블 또는 속성)의 위치 까지는 들어가지는 않은 모습이다.
    private DatabaseReference databaseReference = database.getReference();

    Button btn;
    EditText edit1, edit2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.btn); //버튼 아이디 연결
        edit1 = findViewById(R.id.edit1); //동물 이름 적는 곳
        edit2 = findViewById(R.id.edit2); //동물 종류 적는 곳


        //버튼 누르면 값을 저장
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int address = Integer.parseInt(edit1.getText().toString());
                int room = Integer.parseInt(edit2.getText().toString());
                address = 12345;
                room = 109;
                //에딧 텍스트 값을 문자열로 바꾸어 함수에 넣어줍니다.
                LogIn(address,room);
                putNoise(address,room);
                //addinfor(edit1.getText().toString(),edit2.getText().toString());
            }
        });
    }

    //값을 파이어베이스 Realtime database로 넘기는 함수
    public void addinfor(int address) {
        //여기에서 직접 변수를 만들어서 값을 직접 넣는것도 가능합니다.
        // ex) 갓 태어난 동물만 입력해서 int age=1; 등을 넣는 경우

        //animal.java에서 선언했던 함수.

        //child는 해당 키 위치로 이동하는 함수입니다.
        //키가 없는데 "zoo"와 name같이 값을 지정한 경우 자동으로 생성합니다.
        //databaseReference.child("zoo").child(name).setValue(animal);

    }

    public void LogIn(int address, int room) { //로그인시, db로 데이터 전송
        databaseReference.child(Integer.toString(address)).child(Integer.toString(room)).setValue(room);
    }

    public void Complaint(int address, int room, String content) { //민원 접수 시
        int other_room = 102; //민원 대상 호수
        int result = CheckComplaint(other_room); //상대방 집에서 소음 발생 여부 확인
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        String time = sdf1.format(now);
        Complaint fcomplaint = new Complaint(time,content,result);

        databaseReference.child(Integer.toString(address)).child(Integer.toString(room)).child("fileComplaint").setValue(fcomplaint); //db에 민원 접수내역 저장
        if(result == 1) {
            result = 2;
            Complaint rcomplaint = new Complaint(time,content,result);
            databaseReference.child(Integer.toString(address)).child(Integer.toString(other_room)).child("getComplaint").setValue(rcomplaint); //db에 상대방 집에 민원 저장
        }
    }

    public int CheckComplaint(int other_room) { //소음 발생 여부 확인
        return 1;
    }

    public void putNoise(int address, int room) { //list 가져온 후 값 추가하기
        HashMap<Integer, Integer> noise = new HashMap<>();
        databaseReference.child(Integer.toString(address)).child(Integer.toString(room)).child("fileComplaint").setValue(NoiseSet(noise));
    }

    public HashMap<Integer, Integer> NoiseSet(HashMap<Integer, Integer> noise) { //db로부터 값 가져오기 (현재는 랜덤으로 입력)
       for(int i=0;i<12;i++) {
            noise.put(i,(int)((Math.random()*10000)%100));
        }
        return noise;
    }
}