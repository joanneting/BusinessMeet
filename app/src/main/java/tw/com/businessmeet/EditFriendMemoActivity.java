package tw.com.businessmeet;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;

import tw.com.businessmeet.bean.FriendCustomizationBean;
import tw.com.businessmeet.dao.FriendCustomizationDAO;
import tw.com.businessmeet.helper.AsyncTaskHelper;
import tw.com.businessmeet.helper.DBHelper;
import tw.com.businessmeet.service.Impl.FriendCustomizationServiceImpl;

public class EditFriendMemoActivity extends AppCompatActivity {

    private EditText addColumnMemo, addChipMemo;
    private ChipGroup chipGroup;
    private Button comfirmButton, cancelButton;
    private String title, content;
    private String originalChipContent, updateChipContent, deleteChipContent;
    private String[] originalChipContentSplit, updateChipContentSplit, deleteChipContentSplit;
    private FriendCustomizationBean fcb = new FriendCustomizationBean();
    private FriendCustomizationDAO friendCustomizationDAO;
    private DBHelper dbHelper;
    private FriendCustomizationBean friendCustomizationBean = new FriendCustomizationBean();
    private FriendCustomizationServiceImpl friendCustomizationServiceImpl = new FriendCustomizationServiceImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_profile_edit);
        LayoutInflater inflater = this.getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = inflater.inflate(R.layout.friend_add_column, null);
        builder.setView(view);
        builder.create();
        AlertDialog alertDialog = builder.show();

        addColumnMemo = (EditText) view.findViewById(R.id.addColumn_dialog_Input);
        addColumnMemo.append(this.getIntent().getStringExtra("name"));
        addChipMemo = (EditText) view.findViewById(R.id.addTag_dialog_Input);
        chipGroup = (ChipGroup) view.findViewById(R.id.addTag_dialog_selectedBox);
        originalChipContent = this.getIntent().getStringExtra("content");
        originalChipContentSplit = originalChipContent.split(",");
        for (int i = 1; i < originalChipContentSplit.length; i++) {
            Chip chip = new Chip(this);
            ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(this, null, 0, R.style.Widget_MaterialComponents_Chip_Action);
            chip.setChipDrawable(chipDrawable);
            chip.setText(originalChipContentSplit[i]);
            chip.setCloseIconVisible(true);
            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteChipContent = deleteChipContent + "," + chip.getText();
                    chipGroup.removeView(chip);
                }
            });
            chipGroup.addView(chip);
        }
        addChipMemo.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    originalChipContent = originalChipContent + "," + addChipMemo.getText().toString();
                    Chip chip = new Chip(view.getContext());
                    ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(view.getContext(), null, 0, R.style.Widget_MaterialComponents_Chip_Action);
                    chip.setChipDrawable(chipDrawable);
                    chip.setText(addChipMemo.getText().toString());
                    chip.setCloseIconVisible(true);
                    chip.setOnCloseIconClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            chipGroup.removeView(chip);
                        }
                    });
                    chipGroup.addView(chip);
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    addChipMemo.setText("");
                    return true;
                }
                return false;
            }
        });
        comfirmButton = (Button) view.findViewById(R.id.addColumn_dialog_confirmButton);
        comfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                originalChipContentSplit = originalChipContent.split(",");
                if (deleteChipContent != null) {
                    deleteChipContentSplit = deleteChipContent.split(",");
                    for (int i = 1; i < originalChipContentSplit.length; i++) {
                        for (int j = 1; j < deleteChipContentSplit.length; j++) {
                            System.out.println("original = " + originalChipContentSplit[i] + "   delete = " + deleteChipContentSplit[j]);
                            if (originalChipContentSplit[i].equals(deleteChipContentSplit[j])) {
                                break;
                            }
                            updateChipContent = updateChipContent + "," + originalChipContentSplit[i];
                        }
                    }
                } else {
                    updateChipContent = originalChipContent;
                }
                fcb.setFriendCustomizationNo(getIntent().getIntExtra("friendCustomizationNo", 0));
                fcb.setFriendNo(getIntent().getIntExtra("friendNo", 0));
                fcb.setName(addColumnMemo.getText().toString());
                fcb.setContent(updateChipContent);
                if (checkData(fcb)) {
                    AsyncTaskHelper.execute(() -> FriendCustomizationServiceImpl.update(fcb), friendCustomizationBean -> {
                        friendCustomizationDAO.update(friendCustomizationBean);
                        changeToAnotherPage();

                    });
                    originalChipContent = "";
                    deleteChipContent = "";
                    updateChipContent = "";
                    if (alertDialog.isShowing()) {
                        alertDialog.dismiss();

                    }
                }

            }
        });
        cancelButton = (Button) view.findViewById(R.id.addColumn_dialog_cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alertDialog.isShowing()) {
                    alertDialog.dismiss();
                    changeToAnotherPage();
                }
            }
        });
    }

    private void openDB() {
        dbHelper = new DBHelper(this);
        friendCustomizationDAO = new FriendCustomizationDAO(dbHelper);
    }

    private boolean checkData(FriendCustomizationBean friendCustomizationBean) {
        if (fcb.getName() == null || fcb.getName().equals("")) {
            Toast.makeText(this, "未輸入欄位名稱", Toast.LENGTH_LONG).show();
        } else if ((fcb.getContent() == null || fcb.getContent().equals("")) && (addChipMemo == null || addChipMemo.getText().toString().equals(""))) { //輸入備註和chip皆不可為空
            Toast.makeText(this, "未輸入備註", Toast.LENGTH_LONG).show();
        } else if (addChipMemo.getText().toString().length() >= 1) {
            Toast.makeText(this, "備註輸入完請按Enter確認變成標籤後，才能夠新增備註", Toast.LENGTH_LONG).show();
        } else if (addChipMemo.getText().toString().equals("")) {
            return true;
        } else {
            return true;
        }
        return false;
    }

    public void changeToAnotherPage() {
        Intent intent = new Intent(this, EditFriendsProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("friendId", getIntent().getStringExtra("friendId"));
        bundle.putInt("friendNo", getIntent().getIntExtra("friendNo", 0));
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
