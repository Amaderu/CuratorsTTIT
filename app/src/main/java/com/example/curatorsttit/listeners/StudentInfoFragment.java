package com.example.curatorsttit.listeners;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Group;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.curatorsttit.R;
import com.example.curatorsttit.common.DataGenerator;
import com.example.curatorsttit.common.DateConverter;
import com.example.curatorsttit.databinding.CommonInfoItemBinding;
import com.example.curatorsttit.infoView.InfoRecAdapter;
import com.example.curatorsttit.databinding.FragmentStudentInfoBinding;
import com.example.curatorsttit.databinding.FragmentStudentInfoTwoBinding;
import com.example.curatorsttit.models.Addresses;
import com.example.curatorsttit.models.Passport;
import com.example.curatorsttit.models.Person;
import com.example.curatorsttit.models.StudentData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class StudentInfoFragment extends Fragment {
    private int personID;
    private Toolbar toolbar;
    private Person person;
    FragmentStudentInfoBinding binding;
    FragmentStudentInfoTwoBinding binding2;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() !=  null)
            personID = getArguments().getInt("personID", -1);
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        person = gson.fromJson( getArguments().getString("person"),Person.class);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        toolbar = (Toolbar) getView().findViewById(R.id.toolbar);//.findViewById(R.id.toolbar);
        //toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case android.R.id.home:
                        getActivity().onBackPressed();
                        break;
                    case -1:
                        getActivity().onBackPressed();
                        break;
                    case android.R.id.accessibilitySystemActionBack:
                        getActivity().onBackPressed();
                        break;

                    default: break;
                }
            }
        });
        //toolbar.setNavigationOnClickListener(view -> getActivity().onBackPressed());

        toolbar.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
                menuInflater.inflate(R.menu.action_bar_edit, menu);

                menu.findItem(R.id.edit).setOnMenuItemClickListener(menuItem -> {
                    menu.findItem(R.id.done).setVisible(true);
                    menu.findItem(R.id.cancel).setVisible(true);
                    menu.findItem(R.id.edit).setVisible(false);
                    canEdit(true);
                    return onOptionsItemSelected(menuItem);
                });

                menu.findItem(R.id.cancel).setOnMenuItemClickListener(menuItem -> {
                    menu.findItem(R.id.edit).setVisible(true);
                    menu.findItem(R.id.cancel).setVisible(false);
                    menu.findItem(R.id.done).setVisible(false);
                    canEdit(false);
                    return onOptionsItemSelected(menuItem);
                });

                menu.findItem(R.id.done).setOnMenuItemClickListener(menuItem -> {
                    menu.findItem(R.id.edit).setVisible(true);
                    menu.findItem(R.id.cancel).setVisible(false);
                    menu.findItem(R.id.done).setVisible(false);
                    canEdit(false);
                    return onOptionsItemSelected(menuItem);
                });
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        });
        //toolbar.inflateMenu(R.menu.action_bar_edit);
        //ActionBar bar = getActivity().setActionBar(android.widget.Toolbar);
        //bar.setCustomView(toolbar);
        //((MainActivity)getActivity()).setActionBar(toolbar);
        //toolbar.animate().translationY(-toolbar.getBottom()).setInterpolator(new AccelerateInterpolator()).start();
        //toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
        //ActionBar actionBar = ((MainActivity)getActivity()).getActionBar();
        //actionBar.
        //actionBar.setDisplayShowTitleEnabled(false);
        completeFields(DataGenerator.mockGetStudent(person));
        canEdit(false);
    }
    void completeFields(StudentData studentData){
        binding2.commonLay.studentSNP.setText(studentData.getPerson().getSNP());
        binding2.commonLay.studentBirth.setText(studentData.getBirthday().toString());
        binding2.commonLay.studentBirth.setText(
            DateConverter.getNyFormattedDate(studentData.getBirthday().toString())
        );
        binding2.commonLay.studentYears.setText(String.valueOf(getAge(studentData.getBirthday())));
        binding2.commonLay.studentEmail.setText(studentData.getPerson().getEmail());
        binding2.commonLay.studentPhone.setText(studentData.getPerson().getPhone());

        //инн
        binding2.medLay.studentITN.setText(studentData.getiTN());
        //снилс
        binding2.medLay.studentINOILA.setText(studentData.getInsurPolicy());
        //полис
        binding2.medLay.studentIMPNSSeria.setText(studentData.getMedPolicy().substring(0,3));
        binding2.medLay.studentIMPNSNumber.setText(studentData.getMedPolicy().substring(2));

        Person careTaker = null;
        if(!studentData.getPerens().isEmpty())
            careTaker = studentData.getPerens().get(0);
        if(careTaker != null)
            binding2.caretakertLay.FatherSNP.setText(careTaker.getSNP());
            binding2.caretakertLay.FatherNumber.setText(careTaker.getPhone());
    }


    //FixME получаю tru если могу редактировать
    void canEdit(boolean isEditable){
        getView().clearFocus();
        RelativeLayout rootLayout = getView().findViewById(R.id.expandable);
        EditText snp,birth,years;
        snp = (EditText)rootLayout.findViewById(R.id.studentSNP);
        birth = (EditText)rootLayout.findViewById(R.id.studentBirth);
        years = (EditText)rootLayout.findViewById(R.id.studentYears);
        androidx.constraintlayout.widget.Group group = new Group(requireContext());
        //commonLay
        /*binding2.commonLay.studentSNP.setEnabled(isEditable);
        binding2.commonLay.studentBirth.setEnabled(isEditable);
        binding2.commonLay.studentYears.setEnabled(isEditable);
        binding2.commonLay.studentEmail.setEnabled(isEditable);
        binding2.commonLay.studentPhone.setEnabled(isEditable);*/
        if(!isEditable){
            binding2.commonLay.studentSNP.setInputType(InputType.TYPE_NULL);
            binding2.commonLay.studentBirth.setInputType(InputType.TYPE_NULL);
            binding2.commonLay.studentYears.setInputType(InputType.TYPE_NULL);
            binding2.commonLay.studentEmail.setInputType(InputType.TYPE_NULL);
            binding2.commonLay.studentPhone.setInputType(InputType.TYPE_NULL);
            //инн
            binding2.medLay.studentITN.setInputType(InputType.TYPE_NULL);
            //снилс
            binding2.medLay.studentINOILA.setInputType(InputType.TYPE_NULL);
            //полис
            binding2.medLay.studentIMPNSSeria.setInputType(InputType.TYPE_NULL);
            binding2.medLay.studentIMPNSNumber.setInputType(InputType.TYPE_NULL);

            //caretakertLay
            binding2.caretakertLay.FatherSNP.setInputType(InputType.TYPE_NULL);
            binding2.caretakertLay.FatherNumber.setInputType(InputType.TYPE_NULL);

        } else{
            binding2.commonLay.studentSNP.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
            binding2.commonLay.studentBirth.setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
            binding2.commonLay.studentEmail.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS|InputType.TYPE_TEXT_FLAG_AUTO_CORRECT|InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
            binding2.commonLay.studentPhone.setInputType(InputType.TYPE_CLASS_PHONE);

            //инн
            binding2.medLay.studentITN.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
            //снилс
            binding2.medLay.studentINOILA.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
            //полис
            binding2.medLay.studentIMPNSSeria.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
            binding2.medLay.studentIMPNSNumber.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);

            //caretakertLay
            binding2.caretakertLay.FatherSNP.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
            binding2.caretakertLay.FatherNumber.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
        }

        if(isEditable || binding2.commonLay.studentPhone.getText().toString().isEmpty()){
            binding2.commonLay.studentPhone.setOnClickListener(null);
        } else {
            binding2.commonLay.studentPhone.setOnClickListener(makeCall());
            binding2.commonLay.studentPhone.setTextColor(getResources().getColorStateList(R.color.editable, getActivity().getTheme()));
        }
        if(isEditable || binding2.commonLay.studentEmail.getText().toString().isEmpty()){
            binding2.commonLay.studentEmail.setOnClickListener(null);
        } else {
            binding2.commonLay.studentEmail.setOnClickListener(makeMail());
            binding2.commonLay.studentEmail.setTextColor(getResources().getColorStateList(R.color.editable, getActivity().getTheme()));
        }


    }

    //TODO функция выччисления возраста
    private int getAge(@NonNull Date birthday)
    {
        GregorianCalendar today = new GregorianCalendar();
        GregorianCalendar bday = new GregorianCalendar();
        GregorianCalendar bdayThisYear = new GregorianCalendar();

        bday.setTime(birthday);
        bdayThisYear.setTime(birthday);
        bdayThisYear.set(Calendar.YEAR, today.get(Calendar.YEAR));

        int age = today.get(Calendar.YEAR) - bday.get(Calendar.YEAR);

        if(today.getTimeInMillis() < bdayThisYear.getTimeInMillis())
            age--;

        return age;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStudentInfoBinding.inflate(inflater, container, false);
        binding2 = FragmentStudentInfoTwoBinding.inflate(inflater, container, false);
        //View view =inflater.inflate(R.layout.fragment_student_info, container, false);
        View view = binding2.getRoot();

        /*int count_of_parents = 2;
        if(count_of_parents>1){
            binding2.caretakertLay.expandable4.addView(inflater.inflate(R.layout.careteker_list_item, null));
            ConstraintLayout constraintLayout = binding2.caretakertLay.getRoot();
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.connect(R.id.imageView,ConstraintSet.RIGHT,R.id.caretaker,ConstraintSet.RIGHT,0);
            constraintSet.connect(R.id.imageView,ConstraintSet.TOP,R.id.caretaker,ConstraintSet.TOP,0);
            constraintSet.applyTo(constraintLayout);
        }*/
        loadStudentInfo(person);
        return view;
    }
    View.OnClickListener makeCall(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                String number =((EditText)v).getText().toString();
                callIntent.setData(Uri.parse("tel:"+number));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(callIntent);
            }
        };
    }
    View.OnClickListener makeMail(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText)v;
                String emails = editText.getText().toString();
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"+emails)); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL, emails);
                /*if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                }*/
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, "Send email..."));
            }
        };
    }

    public void show(View view){
        Toast.makeText(getContext(), "Show more", Toast.LENGTH_SHORT).show();
    }
    //TODO написать загрузку/редактирование данных студента
    private void loadStudentInfo(int personID){
        Toast.makeText(requireContext(),String.valueOf(personID), Toast.LENGTH_SHORT);
    }
    private void loadStudentInfo(Person person){
        binding2.commonLay.studentSNP.setText(person.getSNP());
        binding2.commonLay.studentEmail.setText(person.getEmail());
        binding2.commonLay.studentPhone.setText(person.getPhone());
        Toast.makeText(requireContext(),String.valueOf(person.getId()), Toast.LENGTH_SHORT);
    }

}