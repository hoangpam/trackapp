package com.example.drivercar.driverCarPanel;

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
import android.widget.TableLayout;
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
import androidx.viewpager.widget.ViewPager;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.example.drivercar.R;
import com.example.drivercar.activity.MainMenu;
import com.example.drivercar.adapter_Driver.AdapterCars;
import com.example.drivercar.adapter_Driver.AdapterFun;
import com.example.drivercar.adapter_Driver.AdapterProductDriver;
import com.example.drivercar.adapter_customer.AdapterInfomation;
import com.example.drivercar.model.ModelCars;
import com.example.drivercar.model.ModelDriver;
import com.example.drivercar.model.ModelInfomation;
import com.example.drivercar.object.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
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


    private TabLayout TabTab;
    private TabItem TabLoading,TagComple,TabMyseft;
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_driver_home,null);

        setHasOptionsMenu(true);

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
                    Toasty.info(getContext(), "??ang t??m ki???m th??? b???n mu???n vui l??ng ch??? \uD83E\uDD69", Toast.LENGTH_SHORT, true).show();

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
                builder.setTitle("T??n lo???i xe")
                        .setItems(Constants.vehicleTypeName, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //get selected item
                                String selected = Constants.vehicleTypeName[which];

                                filterProductTv.setText(selected);
                                if(selected.equals("T???t c??? lo???i xe")){
                                    //load all
//                                    loadAllCars();
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

        TabTab = (TabLayout) v.findViewById(R.id.TabTab);
        TabLoading = (TabItem) v.findViewById(R.id.TabLoading);
        TagComple = (TabItem) v.findViewById(R.id.TagComple);
        TabMyseft = (TabItem) v.findViewById(R.id.TabMyseft);
        viewPager = (ViewPager) v.findViewById(R.id.viewPager);

        final AdapterFun adapter = new AdapterFun(getContext(),getParentFragmentManager(), TabTab.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(TabTab));

        TabTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        return v;
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
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"T??m ki???m lo???i xe b???n mu???n ??? ????y");
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
//            Toasty.error(getContext(), "L???i k??a s???a g???p\n"+e.getMessage(), Toast.LENGTH_SHORT,true).show();
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
        reference.child("Cars")
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


                        }
//                        //setup adapter
                        adapterCars  =new AdapterCars(getContext(),carsList);
//                        //set adapter
//                        recyclerView.setAdapter(adapterCars);
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
                Toasty.error(getContext(), "Ng??n ng??? kh??ng ???????c h??? tr???", Toast.LENGTH_SHORT,true).show();
            } else {
                voiceProductBtn.setEnabled(true);
                speak();
            }

        } else {
            Log.e("TTS", "Initilization Failed");
            Toasty.error(getContext(), "L???i k??a s???a g???p\n"+ Log.e("TTS", "Initilization Failed"), Toast.LENGTH_SHORT,true).show();
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
                    searchProductEt.setText("Chuy???n ?????i th??nh ch??? b??? l???i");
                }
//                else
//                {
//                    searchProductEt.setText("Kh??ng t??m th???y th??? b???n c???n t??m");
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
                Toasty.success(getContext(), "B???n ???? c???p quy???n truy c??p gi???ng n??i",Toast.LENGTH_SHORT,true).show();
            }
            else{
                Toasty.error(getContext(), "B???n ch??a c???p quy???n truy c??p gi???ng n??i",Toast.LENGTH_SHORT,true).show();
            }
        }
    }
}

