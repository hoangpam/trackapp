package com.example.drivercar.driverCar_fagment;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.example.drivercar.R;
import com.example.drivercar.activity.MainMenu;
import com.example.drivercar.adapter_Driver.AdapterCars;
import com.example.drivercar.adapter_Driver.AdapterProductDriver;
import com.example.drivercar.adapter_customer.AdapterInfomation;
import com.example.drivercar.model.ModelCars;
import com.example.drivercar.model.ModelDriver;
import com.example.drivercar.model.ModelInfomation;
import com.example.drivercar.object.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class DriverHomeFragment extends Fragment implements TextToSpeech.OnInitListener{

    private TextToSpeech tts;
    private RelativeLayout productRL,allproductRL;
    RecyclerView recyclerView,recyclerView1,recyclerView2;
    private EditText searchProductEt;
    private ImageButton filterProductBtn,voiceProductBtn;
    private TextView filterProductTv;
    private ImageButton logoutbtn;
    private static final int REQUEST_CODE_SPEECH_INPUT = 100;


    FirebaseAuth firebaseAuth;
    List<ModelDriver> list;
    List<ModelCars> listcars;
    List<ModelInfomation> listinfomation;


    private ArrayList<ModelCars> carsList;
    private AdapterCars adapterCars;

    private ArrayList<ModelInfomation> infomationsList;
    private AdapterInfomation adapterInfomation;

    TextToSpeech.OnInitListener listener ;
    int count = 0;
    SpeechRecognizer speechRecognizer;
    private TextView nameTv, tabCompleTv, tabMySefTv,tabLoadingTv;
    private Button AddDriverBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_driver_home,null);


        setHasOptionsMenu(true);

        firebaseAuth = FirebaseAuth.getInstance();

        allproductRL =(RelativeLayout) v.findViewById(R.id.allproductRL);
        productRL =(RelativeLayout) v.findViewById(R.id.productRL);

        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();

        searchProductEt =(EditText) v.findViewById(R.id.searchProductEt);


        // Fire off an intent to check if a TTS engine is installed
        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, REQUEST_CODE_SPEECH_INPUT);

        //search


        searchProductEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try{
//                    filter(s.toString());
                    adapterCars.getFilter().filter(s);
                    tts = new TextToSpeech(getContext(), listener );
                    Toasty.info(getContext(), "Đang tìm kiếm thứ bạn muốn vui lòng chờ \uD83E\uDD69", Toast.LENGTH_SHORT, true).show();

                }catch (Exception e){
                    e.printStackTrace();
//                    Toasty.info(getContext(), "\uD83E\uDD80\n"+e.getMessage(), Toast.LENGTH_SHORT, true).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        filterProductTv =(TextView) v.findViewById(R.id.filterProductTv);

        filterProductBtn =(ImageButton) v.findViewById(R.id.filterProductBtn);

        filterProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Tên loại xe")
                        .setItems(Constants.vehicleTypeName, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //get selected item
                                String selected = Constants.vehicleTypeName[which];

                                filterProductTv.setText(selected);
                                if(selected.equals("Tất cả loại xe")){
                                    //load all
                                    loadAllCars();
                                    adapterCars.getFilter().filter("");
                                }
                                else {
                                    //load filtered
                                    loadFilteredDriver(selected);
                                }
                            }
                        })
                        .show();
            }
        });

        voiceProductBtn =(ImageButton) v.findViewById(R.id.voiceProductBtn);
        nameTv = (TextView) v.findViewById(R.id.nameTv);
        tabCompleTv = (TextView) v.findViewById(R.id.tabCompleTv);
        tabMySefTv = (TextView) v.findViewById(R.id.tabMySefTv);
        tabLoadingTv = (TextView) v.findViewById(R.id.tabLoadingTv);

        voiceProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext());
                final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                clickusevoice();
                if(count==0)
                {
                    //start listening
                    speechRecognizer.startListening(speechRecognizerIntent);
                    speak();
                    speakvoice();
                    count=1;
                }
                else{
                    //stop listening
                    speechRecognizer.stopListening();
                    count =0;
                }

            }
        });



        recyclerView =(RecyclerView) v.findViewById(R.id.Recycle_menu);
        recyclerView1 =(RecyclerView) v.findViewById(R.id.Recycle_menu2);
        recyclerView2 =(RecyclerView) v.findViewById(R.id.Recycle_menu3);



        //thực hiện cho tag 3





        tabCompleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCompleUI();

                recyclerView2.setHasFixedSize(true);
                recyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView2.setItemAnimator(new DefaultItemAnimator());
                adapterCars = new AdapterCars(getContext(),carsList);
                recyclerView2.setAdapter(adapterCars);

            }
        });

        tabMySefTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //load orders
                showMySefUI();
                loadAllCars();
                //thực hiện cho tag 1
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                adapterCars = new AdapterCars(getContext(),carsList);
                recyclerView.setAdapter(adapterCars);

            }
        });

        tabLoadingTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //load product
                showLoadingUI();
                loadAllInfomations();
                //thực hiện cho tag 2
                recyclerView1.setHasFixedSize(true);
                recyclerView1.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                adapterInfomation = new AdapterInfomation(getContext(),infomationsList);
                recyclerView1.setAdapter(adapterInfomation);

            }
        });



        return v;
    }



    private void showMySefUI() {
        //show products ui and hide orders ui
        tabLoadingTv.setTextColor(getResources().getColor(R.color.black));
        tabLoadingTv.setBackgroundResource(R.drawable.shape_rect04);

        tabCompleTv.setTextColor(getResources().getColor(R.color.black));
        tabCompleTv.setBackgroundResource(R.drawable.shape_rect04);

        tabMySefTv.setTextColor(getResources().getColor(R.color.white));
        tabMySefTv.setBackgroundResource(R.drawable.shape_rect03);

        recyclerView.setVisibility(View.VISIBLE );
        recyclerView1.setVisibility(View.GONE);
        recyclerView2.setVisibility(View.GONE);

    }
    private void showLoadingUI() {
        //show orders ui and hide products ui
        tabCompleTv.setTextColor(getResources().getColor(R.color.black));
        tabCompleTv.setBackgroundResource(R.drawable.shape_rect04);

        tabMySefTv.setTextColor(getResources().getColor(R.color.black));
        tabMySefTv.setBackgroundResource(R.drawable.shape_rect04);

        tabLoadingTv.setTextColor(getResources().getColor(R.color.white));
        tabLoadingTv.setBackgroundResource(R.drawable.shape_rect03);


        recyclerView1.setVisibility(View.VISIBLE );
        recyclerView.setVisibility(View.GONE);
        recyclerView2.setVisibility(View.GONE);
    }
    private void showCompleUI() {
        //show orders ui and hide products ui
        tabCompleTv.setTextColor(getResources().getColor(R.color.white));
        tabCompleTv.setBackgroundResource(R.drawable.shape_rect03);

        tabMySefTv.setTextColor(getResources().getColor(R.color.black));
        tabMySefTv.setBackgroundResource(R.drawable.shape_rect04);

        tabLoadingTv.setTextColor(getResources().getColor(R.color.black));
        tabLoadingTv.setBackgroundResource(R.drawable.shape_rect04);


        recyclerView1.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        recyclerView2.setVisibility(View.INVISIBLE);
    }



    private void checkUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user == null)
        {
            Intent intent = new Intent(new Intent(getActivity(), MainMenu.class));
            startActivity(intent);
        }

    }

    private void speak() {
        //intent to show speech to text didlog

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Tìm kiếm loại xe bạn muốn ở đây");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, REQUEST_CODE_SPEECH_INPUT);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                getContext().getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_to_text_info));
        //start intent
        try {
            //in there was no error
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);

        }catch (Exception e){
            //if there was some error
            //get message of error
//            Toasty.error(getContext(), "Lỗi kìa sữa gấp\n"+e.getMessage(), Toast.LENGTH_SHORT,true).show();
        }
    }

    private void filter(String text) {
        carsList = new ArrayList<>();
        for (ModelCars items : listcars) {
            if (items.getVehicleTypeName().toUpperCase().contains(text.toUpperCase())) {
                carsList.add(items);
            }
        }
        adapterCars.setFiltercarList(carsList);
    }

    private void loadFilteredDriver(final String selected) {
        carsList = new ArrayList<>();

        //get all product
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).child("Cars")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //before getting reset list
                        carsList.clear();
                        for (DataSnapshot ds: snapshot.getChildren()){
                            String VehicleTypeName = ""+ds.child("VehicleTypeName").getValue();

                            //if selected category mathes product category then add in list
                            if(selected.equals(VehicleTypeName)){
                                ModelCars modelCars = ds.getValue(ModelCars.class);
                                carsList.add(modelCars);
                            }
//                            if(selected.equals(productTitle)){
//                                ModelDriver modelDriver = ds.getValue(ModelDriver.class);
//                                driverList.add(modelDriver);
//                            }

                        }
                        //setup adapter
                        adapterCars  =new AdapterCars(getContext(),carsList);
                        //set adapter
                        recyclerView.setAdapter(adapterCars);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadAllCars() {
        carsList = new ArrayList<>();

        //get all product
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).child("Cars")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //before getting reset list
                        carsList.clear();
                        for (DataSnapshot ds: snapshot.getChildren()){
                            ModelCars modelCars = ds.getValue(ModelCars.class);
                            carsList.add(modelCars);
                        }
                        //setup adapter
                        adapterCars =new AdapterCars(getContext(),carsList);
                        //set adapter
                        recyclerView.setAdapter(adapterCars);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadAllInfomations() {
        infomationsList = new ArrayList<>();

        //get all product
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).child("Infomations")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //before getting reset list
                        infomationsList.clear();
                        for (DataSnapshot ds: snapshot.getChildren()){
                            ModelInfomation modelInfomation = ds.getValue(ModelInfomation.class);
                            infomationsList.add(modelInfomation);
                        }
                        //setup adapter
                        adapterInfomation =new AdapterInfomation(getContext(),infomationsList);
                        //set adapter
                        recyclerView1.setAdapter(adapterCars);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    @Override
    public void onInit(int status) {
        // TODO Auto-generated method stub
        if(status != TextToSpeech.ERROR) {
            tts.setLanguage(Locale.US);
        }
        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.getDefault());

            // tts.setPitch(5); // set pitch level

            // tts.setSpeechRate(2); // set speech speed rate

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                Log.e("TTS", "Language is not supported");
                Toasty.error(getContext(), "Ngôn ngữ không được hỗ trợ", Toast.LENGTH_SHORT,true).show();
            } else {
                voiceProductBtn.setEnabled(true);
                speak();
            }

        } else {
            Log.e("TTS", "Initilization Failed");
            Toasty.error(getContext(), "Lỗi kìa sữa gấp\n"+ Log.e("TTS", "Initilization Failed"), Toast.LENGTH_SHORT,true).show();
        }

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.logout,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        return super.onOptionsItemSelected(item);
    }



    @Override
    public  void onActivityResult(int requestCode,int resultCode,@Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        switch (requestCode){
            case REQUEST_CODE_SPEECH_INPUT:{
                if(requestCode == RESULT_OK && null!=data ){
//                    //get text array from voice intent
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//                    //set to text
                    searchProductEt.setText(result.get(0));
                    String tex = searchProductEt.getText().toString().trim();
                    String toSpeak = tex;
                    Toasty.success(getContext(), toSpeak,Toast.LENGTH_SHORT,true).show();
                    tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                }
                else{
                    searchProductEt.setText("Chuyển đổi thành chữ bị lỗi");
                }
//                else
//                {
//                    searchProductEt.setText("Không tìm thấy thứ bạn cần tìm");
//                    // missing data, install it
//                }
                break;
            }
        }
    }

    public void speak(String text) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    public void clickusevoice()
    {
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.RECORD_AUDIO},1);
        }

    }
    public void speakvoice()
    {
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> data = results.getStringArrayList(speechRecognizer.RESULTS_RECOGNITION);

                searchProductEt.setText(data.get(0));
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == 1)
        {
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Toasty.success(getContext(), "Bạn đã cấp quyền truy câp giọng nói",Toast.LENGTH_SHORT,true).show();
            }
            else{
                Toasty.error(getContext(), "Bạn chưa cấp quyền truy câp giọng nói",Toast.LENGTH_SHORT,true).show();
            }
        }
    }
}

