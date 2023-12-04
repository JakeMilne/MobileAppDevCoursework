package com.example.mobileappdevcoursework;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mobileappdevcoursework.data.DatabaseRepository;
import com.example.mobileappdevcoursework.data.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
//User profile fragment - allows the User to change their name/ league preferences, which is then stored in the UserDatabase
public class UserProfile extends Fragment {



    private DatabaseRepository databaseRepository;
    int league;

    public UserProfile() {
        // Required empty public constructor
    }


    public static UserProfile newInstance(String param1, String param2) {
        UserProfile fragment = new UserProfile();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        new Thread(new Runnable() {
            @Override
            public void run() {
                String name = databaseRepository.getName();
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        
                        nameText.setText(name); // getting the same and displaying it in the name editText
                    }
                });
            }
        }).start();
        //spinner code from https://code.tutsplus.com/how-to-add-a-dropdown-menu-in-android-studio--cms-37860t, accessed 26th November 2023

        Spinner spinnerLanguages = view.findViewById(R.id.leagueSpinner); //dropdown used for selecting leagues

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(), R.array.leagues, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerLanguages.setAdapter(adapter);

        spinnerLanguages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Display a Toast with the selected item

                Toast.makeText(requireContext(), "Selected: " + parentView.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                switch(parentView.getItemAtPosition(position).toString()) { //converting league names into ids for api calls
                    case "Scottish Premiership":
                       
                        league=501;
                        break;
                    case "Scottish Premiership Play-offs":
                        league=513;
                       
                        break;
                    case "Danish Superliga":
                        league=271;
                        break;

                    case "Danish Superliga Play-offs":
                        league=1659;
                        break;
                        default:
                        league=501; //if it breaks for some reason this should just set it to the scottish premiership
                        
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

        //saves the users data to the UserDatabase
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameText.getText().toString();


                final String finalName = name;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        databaseRepository.deleteUser();
                        //since UserDatabase should only have 1 record this deletes the existing
                        // user if they click the save button, then adds them again with the new data
                        String name = "";
                        if(finalName == null || finalName.isEmpty()){

                            name = databaseRepository.getName();
//                            userUpdate.setName(oldName);

                        }else{

                            name = finalName;
                        }

                        User userUpdate = new User(name, league);

                        databaseRepository.updateUser(userUpdate);


                    }
                }).start();
                Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();

            }
        });

        return view;
    }
}
