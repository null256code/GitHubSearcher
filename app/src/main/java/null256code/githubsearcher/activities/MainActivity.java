package null256code.githubsearcher.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import null256code.githubsearcher.R;
import null256code.githubsearcher.network.SearchRepositoryAsyncTask;

public class MainActivity extends AppCompatActivity {

    static boolean sLoadRepository = false; //リポジトリ読み込み中フラグ

    // layoutのラジオボタンとvalueを紐付けるMap
    //「SparseArray」は要素数多くないとパフォーマンスに差はないらしいので使わない
    private static final Map<Integer, String> SORT_ITEM_MAP;
    static {
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(R.id.sortStarRadio,"stars");
        map.put(R.id.sortForkRadio,"forks");
        map.put(R.id.sortUpdateRadio,"updated");
        map.put(R.id.sortNothingRadio,"");
        SORT_ITEM_MAP = map;
    }

    /** 入力した内容でGitHubを検索して、Activityに表示します */
    private void loadRepositoryAsync() {
        EditText keyword = (EditText) findViewById(R.id.keywordForm);
        RadioGroup sort = (RadioGroup) findViewById(R.id.sortSelect);
        CheckBox order = (CheckBox) findViewById(R.id.orderCheck);

        String keywordStr = keyword.getText().toString();
        if(StringUtils.isBlank(keywordStr)) {
            return;
        }
        String sortStr = SORT_ITEM_MAP.get(sort.getCheckedRadioButtonId());;
        String orderStr = order.isChecked() ? "desc" : "asc";

        sLoadRepository = true;
        SearchRepositoryAsyncTask task = new SearchRepositoryAsyncTask(new SearchRepositoryCallBackTask(MainActivity.this));
        task.updateSearchCondition(keywordStr, sortStr, orderStr);
        task.execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.searchButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sLoadRepository) {
                    Toast.makeText(MainActivity.this, "検索中です..検索が終わるまでお待ち下さい。", Toast.LENGTH_SHORT).show();
                } else {
                    loadRepositoryAsync();
                }
            }
        });

        EditText keywordForm = (EditText) findViewById(R.id.keywordForm);
        keywordForm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                Log.d("Form", "▼フォーム値");
                Log.d("Form", editable.toString());
                if(sLoadRepository) {
                    return; //処理中はテキストが変わっても何もしない。
                } else if (editable.length() == 0) {
                    return; //テキストが空っぽのときも何もしない。
                } else {
                    //▼変換前判定
                    boolean unfixed = false;
                    Object[] spanned = editable.getSpans(0, editable.length(), Object.class);
                    if (spanned != null) {
                        for (Object obj : spanned) {
                            // UnderlineSpan での判定から getSpanFlags への判定に変更。
                            // if (obj instanceof android.text.style.UnderlineSpan) {
                            if ((editable.getSpanFlags(obj) & Spanned.SPAN_COMPOSING) == Spanned.SPAN_COMPOSING) {
                                unfixed = true;
                                break;
                            }
                        }
                    }
                    Log.d("変換前", String.valueOf(unfixed));
                    if(unfixed) {
                        return; //変換前ならば、何もしない
                    }
                    loadRepositoryAsync();
                }
            }
        });
    }
}

/** リポジトリ検索機能のうち、Activityに変更を加えるもの */
class SearchRepositoryCallBackTask implements SearchRepositoryAsyncTask.AsyncCallback {

    private Activity activity;
    SearchRepositoryCallBackTask(Activity activity) {
        this.activity = activity;
    }

    public void preExecute() {}
    public void progressUpdate(int progress) {}
    public void postExecute(JSONObject result) {
        if(result == null || result.isNull(SearchRepositoryAsyncTask.JSON_KEY_INCOMPLETE_RESULT)) {
            //JSON取得失敗
            Toast.makeText(activity, "データの取得に失敗しました。", Toast.LENGTH_LONG).show();
            MainActivity.sLoadRepository = false;
            return;
        }
        try {
            boolean incomplete = result.getBoolean(SearchRepositoryAsyncTask.JSON_KEY_INCOMPLETE_RESULT);
            if(incomplete) {
                //検索失敗(GitHub側)
                Toast.makeText(activity, "接続がタイムアウトしました。", Toast.LENGTH_LONG).show();
                MainActivity.sLoadRepository = false;
                return;
            }
            //Integer totalCount = result.getInt(SearchRepositoryAsyncTask.JSON_KEY_TOTAL_COUNT);
            JSONArray items = result.getJSONArray(SearchRepositoryAsyncTask.JSON_KEY_ITEMS); //検索結果の配列
            ArrayList<RepositoryInfo> list = new ArrayList<>();
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                RepositoryInfo info = new RepositoryInfo();
                info.setId(item.getInt(SearchRepositoryAsyncTask.JSON_KEY_ID));

                JSONObject owner = item.getJSONObject(SearchRepositoryAsyncTask.JSON_KEY_OWNER);
                info.setOwnerLogin(owner.getString(SearchRepositoryAsyncTask.JSON_KEY_OWNER_LOGIN));
                String avatarURL = owner.getString(SearchRepositoryAsyncTask.JSON_KEY_OWNER_AVATAR);

                info.setHtmlURL(item.getString(SearchRepositoryAsyncTask.JSON_KEY_HTML_URL));
                info.setDescription(item.getString(SearchRepositoryAsyncTask.JSON_KEY_DESCRIPTION));
                info.setLanguage(item.getString(SearchRepositoryAsyncTask.JSON_KEY_LANGUAGE));
                list.add(info);
            }

            // 受信したデータでListを作成し、Viewを更新する
            RepositoryInfoAdapter adapter = new RepositoryInfoAdapter(activity);
            adapter.setRepositoryList(list);
            ListView listView = (ListView)activity.findViewById(R.id.resultList);
            listView.setAdapter(adapter);
            //リストをクリックした場合、ブラウザでそのURLを開く
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    RepositoryInfo info = (RepositoryInfo) adapterView.getItemAtPosition(i);
                    if(StringUtils.isNotBlank(info.getHtmlURL())) {
                        Uri uri = Uri.parse(info.getHtmlURL());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        activity.startActivity(intent);
                    }
                }
            });

            //データ読み込み中フラグをfalseにする
            MainActivity.sLoadRepository = false;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void cancel() {}
}
