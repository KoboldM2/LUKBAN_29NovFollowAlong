package com.lukban.a29novemberfollowalong;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText etID, etFname, etLname;
    Spinner stSection;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference refStudent;
    List<Student> listStud;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etID = findViewById(R.id.etID);
        etFname = findViewById(R.id.etFName);
        etLname = findViewById(R.id.etLName);
        stSection = findViewById(R.id.stSection);

        refStudent = db.getReference("students");
        listStud = new ArrayList<>();

    }

    @Override
    protected void onStart() {
        super.onStart();
        refStudent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listStud.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    Student stud = ds.getValue(Student.class);
                    listStud.add(stud);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addMethod(View v) {
        String id = refStudent.push().getKey();
        String fname = etFname.getText().toString();
        String lname = etLname.getText().toString();
        String section = stSection.getSelectedItem().toString();

        Student stud = new Student(id, fname, lname, section);
        refStudent.child(id).setValue(stud);
        Toast.makeText(this, "Saved in Firebase",Toast.LENGTH_LONG).show();
    }

    public void moveFirst(View v) {
        Student stud = listStud.get(0);
        etID.setText(stud.getId());
        etFname.setText(stud.getFname());
        etLname.setText(stud.getLname());
        //stSection.set;
    }

    public void moveNext(View v) {
        if (i == listStud.size() - 1) {
            Student stud = listStud.get(listStud.size()-1);
            Toast.makeText(this, "Last record.",Toast.LENGTH_LONG).show();
        }
        Student stud = listStud.get(++i);
        etID.setText(stud.getId());
        etFname.setText(stud.getFname());
        etLname.setText(stud.getLname());
    }

    public void editRecord(View v) {
        String id = etID.getText().toString();
        String fname = etFname.getText().toString();
        String lname = etLname.getText().toString();
        String section = stSection.getSelectedItem().toString();

        Student stud = new Student(id, fname, lname, section);
        refStudent.child(id).setValue(stud);
        Toast.makeText(this, "Saved in Firebase",Toast.LENGTH_LONG).show();
    }

    public void deleteRecord(View v) {
        String id = etID.getText().toString();
        refStudent.child(id).removeValue();
        Toast.makeText(this, "Record deleted",Toast.LENGTH_LONG).show();
    }

}
