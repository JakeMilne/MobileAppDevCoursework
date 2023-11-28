package com.example.mobileappdevcoursework;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link userProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class userProfile extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private databaseRepository databaseRepository;
    int league;
    //databaseRepository = databaseRepository.getRepository(application);
    public userProfile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment userProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static userProfile newInstance(String param1, String param2) {
        userProfile fragment = new userProfile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        databaseRepository = databaseRepository.getRepository(requireContext());

        new Thread(new Runnable() {
            @Override
            public void run() {

                league = databaseRepository.getLeague();


            }
        }).start();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        Button saveBtn = view.findViewById(R.id.saveBtn);

        EditText nameText = view.findViewById(R.id.nameText);


        Spinner spinnerLanguages = view.findViewById(R.id.leagueSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(), R.array.leagues, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerLanguages.setAdapter(adapter);

        spinnerLanguages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Display a Toast with the selected item

                Toast.makeText(requireContext(), "Selected: " + parentView.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                switch(parentView.getItemAtPosition(position).toString()) {
                    case "Scottish PremierShip":
                        // code block
                        league=501;
                        break;
                    case "Scottish PremierShip Play-offs":
                        league=513;
                        // code block
                        break;
                    case "Danish Superliga":
                        league=271;
                        break;

                        default:
                        league=1659;
                        // code block
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameText.getText().toString();


                final String finalName = name;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        databaseRepository.deleteUser();
                        user userUpdate = new user();
                        if(finalName == null || finalName.isEmpty()){

                            String oldName = databaseRepository.getName();
                            userUpdate.setName(oldName);

                        }else{
                            userUpdate.setName(finalName);
                        }



                        userUpdate.setFavouriteLeague(league);
                        databaseRepository.updateUser(userUpdate);


                    }
                }).start();
                Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();

            }
        });

        return view;
    }
}