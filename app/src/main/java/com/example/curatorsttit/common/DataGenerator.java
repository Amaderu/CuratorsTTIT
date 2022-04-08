package com.example.curatorsttit.common;

import android.icu.text.Transliterator;
import android.os.Build;

import com.example.curatorsttit.MainActivity;
import com.example.curatorsttit.R;
import com.example.curatorsttit.data.local.entity.PersonEntity;
import com.example.curatorsttit.data.local.entity.RoleEntity;
import com.example.curatorsttit.data.local.entity.UserEntity;
import com.example.curatorsttit.models.Addresses;
import com.example.curatorsttit.models.Group;
import com.example.curatorsttit.models.Passport;
import com.example.curatorsttit.models.Person;
import com.example.curatorsttit.models.StudentData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Generates data to pre-populate the database
 */
public class DataGenerator {

    private static final String[] FIRST = new String[]{
            "Special edition", "New", "Cheap", "Quality", "Used"};
    private static final String[] SECOND = new String[]{
            "Three-headed Monkey", "Rubber Chicken", "Pint of Grog", "Monocle"};
    private static final String[] DESCRIPTION = new String[]{
            "is finally here", "is recommended by Stan S. Stanman",
            "is the best sold product on Mêlée Island", "is \uD83D\uDCAF", "is ❤️", "is fine"};
    private static final String[] COMMENTS = new String[]{
            "Comment 1", "Comment 2", "Comment 3", "Comment 4", "Comment 5", "Comment 6"};

    private static final String[] USERS = new String[]{
            "Доржинова Лариса Михайловна LarisaDorzhinova997 uaUO3y7wa2t1",
            "Радецкая Берта Семеновна BertaRadetskaya242, CWG5ub1aCtO3",
            "Шмидт Эраст Тимофеевич ErastShmidt674 a7HMuLboGnub"

    };
    private static final String[] Roles = new String[]{
            "Куратор", "Администратор"
    };
    private static final String[] Persones = new String[]{
            "Егоров Степан Богданович",
    "Гришин Фёдор Ярославович",
    "Коновалов Михаил Львович",
    "Ковалева Полина Мироновна",
    "Чеснокова Анастасия Николаевна",
    "Семенова Ульяна Никитична",
    "Булгакова Марина Артёмовна",
    "Антонова Анна Святославовна",
    "Федоров Никита Кириллович",
    "Колесникова Валерия Матвеевна",
    "Новикова Полина Ильинична",
    "Михайлов Александр Антонович",
    "Баженова Софья Васильевна",
    "Коровин Даниил Фёдорович",
    "Петрова Вера Владиславовна",
    "Орлов Глеб Фёдорович",
    "Царев Максим Романович",
    "Захаров Игорь Артёмович",
    "Павлова Ольга Александровна",
    "Грачева Екатерина Матвеевна",
            "Алфимова Светлана Александровна",
            "Шарапова Наталья Александровна"
    };

    public static List<UserEntity> generateUsers() {
        List<UserEntity> users = new ArrayList<>(USERS.length);
        Random rnd = new Random();
        for (int i = 1; i <= USERS.length; i++) {
            UserEntity user = new UserEntity();
            final String[] userDate =  USERS[i-1].split(" ");
            user.setPersonId(i);
            //random.nextInt(max - min) + min; to min-max bound
            user.setRoleId(rnd.nextInt(2)+1);
            user.setLogin(userDate[3]);
            user.setPassword(userDate[4]);
            user.setId(i);
            users.add(user);
        }
        return users;
    }

    public static List<RoleEntity> generateRoles() {
        List<RoleEntity> roles = new ArrayList<>(Roles.length);
        Random rnd = new Random();
        for (int i = 1; i <= 2; i++) {
            RoleEntity role = new RoleEntity();
            role.setId(i);
            role.setName(Roles[i-1]);
            roles.add(role);
        }
        return roles;
    }

    public static List<PersonEntity> generatePersons() {
        List<PersonEntity> persons = new ArrayList<>(USERS.length);
        Random rnd = new Random();
        for (int i = 1; i <= USERS.length; i++) {
            PersonEntity person = new PersonEntity();
            final String[] personDate =  USERS[i-1].split(" ");
            person.setId(i);
            person.setSurname(personDate[0]);
            person.setName(personDate[1]);
            person.setPatronymic(personDate[2]);
            persons.add(person);
        }
        return persons;
    }

    public static List<Person> mockGenerateStudents(Group group) {
        List<Person> listStudents = new ArrayList<>();
        String[] students = null;
        int start = 0;
        if(group.getNumber().startsWith("6")){
            start = 0;
            students  = new String[5];
            System.arraycopy(Persones, start, students, 0, 5);
        }else if(group.getNumber().startsWith("4")){
            start = 6;
            students  = new String[5];
            System.arraycopy(Persones, start, students, 0, 5);
        }
        for (String wp : students) {
            String[] fio = wp.split(" ");
            //String CYRILLIC_TO_LATIN = "Latin-Russian/BGN";
            String CYRILLIC_TO_LATIN = "Russian-Latin/BGN";
            Transliterator toLatinTrans = null;
            String transliterate;
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                toLatinTrans = Transliterator.getInstance(CYRILLIC_TO_LATIN);
                transliterate = toLatinTrans.transliterate(fio[0]);
                /*Enumeration<String> availableIDs = Transliterator.getAvailableIDs();
                for (Enumeration<String> e = availableIDs; e.hasMoreElements();)
                    Log.i("Enumeration", "mockLoadStudents: "+ e.nextElement());*/
            }
            else transliterate = fio[0];
            Random rnd = new Random();
            int n = rnd.nextInt(999999999)+1000000000;
            String randomNumber = "99 999 99 99";
            randomNumber = String.valueOf(n);
            StringBuilder builder = new StringBuilder();
            builder.append(randomNumber.subSequence(0,2))
                    .append(" ")
                    .append(randomNumber.subSequence(2,5))
                    .append(" ")
                    .append(randomNumber.subSequence(5,7))
                    .append(" ")
                    .append(randomNumber.subSequence(7,9));
            randomNumber = builder.toString();
            Person person = new Person(
                    fio[0],
                    fio[1],
                    fio[2],
                    group.getNumber(),
                    transliterate+"@gmail.com",
                    "+7 9"+randomNumber

            );
            listStudents.add(person);
        }
        return listStudents;
    }

    public static List<Group> mockGenerateGroup() {
        List<Group> listGroup = new ArrayList<Group>();
        listGroup.add(new Group(1,"602","Специалисты Инф.Систем"));
        listGroup.add(new Group(2,"482","Web"));
        return listGroup;
    }

    public static StudentData mockGetStudent(Person studentIn){
            Person person = studentIn;

            Person careTaker = new Person("Павлова", "Ольга", "Александровна","olgapavlova@mail.ru","+79131596648");
            person.setId(2);
            List<Person> perens = new ArrayList<Person>();
            perens.add(careTaker);
            Addresses addresses = new Addresses();
            addresses.setId(1);
            addresses.setPostalCode("123123");
            addresses.setRegion("70");
            addresses.setStreet("Котовского");
            addresses.setHouse("18");
            addresses.setFlat("234");
            addresses.setCity("Томск");
            addresses.setCountry("Россия");
            Date date = null;
            try {
                date = new SimpleDateFormat(DateConverter.DATE_TIME_MS_SQL).parse("2002-02-05T00:00:00");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Passport passport = new Passport();
            passport.setId(1);
            passport.setSeries("1324");
            passport.setNumber("123456");
            passport.setIssueDate("2016-02-12T00:00:00");
            passport.setIssuingAuthority("УФМС РФ Томск");
            passport.setSubdivisionCode("12545");
            passport.setRegistrationAddressId(1);

            StudentData data = new StudentData().setDormitory(true).setiTN("345345").setPassport(passport).setMedPolicy("123123123")
                    .setEducationStatus("Учится").setGroupNumber("682").setInsurPolicy("234234234").setPerson(person)
                    .setPerens(perens)
                    .setBirthday(date)
                    .setRegistrationAddress(addresses).setResidentialAddress(addresses);
            return data;
    }
    
    
}
