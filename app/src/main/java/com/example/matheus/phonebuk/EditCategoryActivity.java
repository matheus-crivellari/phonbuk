package com.example.matheus.phonebuk;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class EditCategoryActivity extends Activity {

    public static final int NEW_CATEGORY  = 0;
    public static final int EDIT_CATEGORY = 1;

    private Bundle extras;
    private View[] colorSelector = new View[12];
    private View[] colorSelected = new View[12];
    private int selectedColorIndex = 0;

    private TextView title;
    private EditText categoryNameInputText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        extras = getIntent().getExtras();
        title = (TextView) findViewById(R.id.edit_category_ac);

        categoryNameInputText = (EditText) findViewById(R.id.category_name_input_text);

        setActivityActionBarTitle();
        populateButtonsArrays();
        hideAllSelectedSymbols();
        assignTapEventToColorSelectionButtons();

        if(extras.getInt("action") == EDIT_CATEGORY){
            categoryNameInputText.setText(extras.getString("catName"));
            selectedColorIndex = extras.getInt("catColor");
            updateColorSelectedSymbols(extras.getInt("catColor"));
        }
    }

    /**
     * Sets the Activity Title in Activity's Action Bar based on the action (0 for adding a new
     * Category and 1 for editing an existing one) passed by Extras.
     */
    private void setActivityActionBarTitle(){
        if(extras.getInt("action") == NEW_CATEGORY){
            title.setText(R.string.new_category_title);
        }else if(extras.getInt("action") == EDIT_CATEGORY){
            title.setText(R.string.edit_category_title);
        }
    }

    /**
     * Populates colorSelector and colorSelected arrays for easing the manipulation of massive
     * button quantities.
     */
    private void populateButtonsArrays(){
        for(int i=0;i<colorSelected.length;i++){
            int slctrId = getResources().getIdentifier("clr_slctr_btn"+i,"id",getPackageName());
            int slctdId = getResources().getIdentifier("clr_slctd_btn"+i,"id",getPackageName());

            colorSelector[i] = findViewById(slctrId);
            colorSelected[i] = findViewById(slctdId);
        }
    }

    /**
     * Hides all selected symbols aside each color selector button.
     */
    private void hideAllSelectedSymbols(){
        for(int i=1;i<colorSelected.length;i++){
            colorSelected[i].setVisibility(View.GONE);
        }
    }

    /**
     * Assigns tap event to every Color Selection button.
     */
    private void assignTapEventToColorSelectionButtons(){
        for(int i=0;i<colorSelector.length;i++){
            final View selectorButton = colorSelector[i];
            final int idx = i;

            selectorButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedColorIndex = idx;
                    updateColorSelectedSymbols(selectedColorIndex);
                }
            });
        }
    }

    /**
     * Updates the exhibition of color selection button's selected symbol of.
     * @param selectedColorIndex Selected Color index. An integer that  can be translated by
     *                           the @see {@link com.example.matheus.phonebuk.Color}
     *                           class.
     */
    private void updateColorSelectedSymbols(int selectedColorIndex){
        for(int i=0;i<colorSelected.length;i++){
            if(i == selectedColorIndex){
                colorSelected[i].setVisibility(View.VISIBLE);
            }else{
                colorSelected[i].setVisibility(View.GONE);
            }
        }
    }

    /**
     * Either saves a new Category or updates an already existing Category based on the action
     * (0 for adding a new Category and 1 for editing an existing one) passed by Extras.
     */
    public void saveCategory(){
        if(extras.getInt("action") == NEW_CATEGORY){
            DataBridge.getDatabaseHelper().insertCategory(categoryNameInputText.getText().toString(),selectedColorIndex,0);
        }else if(extras.getInt("action") == EDIT_CATEGORY){
            DataBridge.getDatabaseHelper().updateCategory(
                    categoryNameInputText.getText().toString(),
                    selectedColorIndex,
                    extras.getInt("catSystem"),
                    extras.getInt("catId")
            );
        }
    }

    /**
     * Event fired when Save Category button is clicked.
     * @param saveButton Represents the view clicked. It's received from the Android API.
     */
    public void onSaveButtonClicked(View saveButton){
        saveCategory();
        finish();
    }

    /**
     * Event fired when Back button is clicked.
     * @param backButton Represents the view clicked. It's received from the Android API.
     */
    public void onBackButtonClicked(View backButton){
        finish();
    }
}