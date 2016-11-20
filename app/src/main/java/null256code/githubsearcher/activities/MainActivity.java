package null256code.githubsearcher.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import null256code.githubsearcher.R;
import null256code.githubsearcher.network.SearchRepositoryAsyncTask;

public class MainActivity extends AppCompatActivity {

    private boolean loadRepository = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.searchButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mSearchRepositoryTask.updateSearchCondition("test", "", "");
//                mSearchRepositoryTask.execute();
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
                if(loadRepository) {
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

                    loadRepository = true;
                    SearchRepositoryAsyncTask task = new SearchRepositoryAsyncTask(
                            new SearchRepositoryAsyncTask.AsyncCallback() {
                                public void preExecute() {}
                                public void progressUpdate(int progress) {}
                                public void postExecute(JSONObject result) {
                                    if(result == null) {
                                        return; //JSON取得失敗
                                    }
                                    try {
                                        boolean incomplete = result.getBoolean(SearchRepositoryAsyncTask.JSON_KEY_INCOMPLETE_RESULT);
                                        if(incomplete) {
                                            return; //検索失敗(GitHub側)
                                        }
                                        Integer totalCount = result.getInt(SearchRepositoryAsyncTask.JSON_KEY_TOTAL_COUNT);
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
                                        // ListView 用のアダプタを作成
                                        RepositoryInfoAdapter adapter = new RepositoryInfoAdapter(MainActivity.this);
                                        adapter.setRepositoryList(list);
                                        // ListView にアダプタをセット
                                        ListView listView = (ListView)findViewById(R.id.resultList);
                                        listView.setAdapter(adapter);
                                        loadRepository = false;
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                public void cancel() {}
                            });
                    task.updateSearchCondition(editable.toString(), "", "");
                    task.execute();
                }
            }
        });
    }
}

class RepositoryInfoAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater = null;
    ArrayList<RepositoryInfo> repositoryList;

    public RepositoryInfoAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setRepositoryList(ArrayList<RepositoryInfo> repositoryList) {
        this.repositoryList = repositoryList;
    }

    @Override
    public int getCount() {
        return repositoryList.size();
    }

    @Override
    public Object getItem(int i) {
        return repositoryList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return repositoryList.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = layoutInflater.inflate(R.layout.repository_row,viewGroup,false);
        if (repositoryList.get(i).getOwnerImg() != null) {
            ((ImageView)view.findViewById(R.id.ownerAvatar)).setImageBitmap(repositoryList.get(i).getOwnerImg());
        }
        ((TextView)view.findViewById(R.id.ownerLogin)).setText(repositoryList.get(i).getOwnerLogin());
        ((TextView)view.findViewById(R.id.repositoryURL)).setText(repositoryList.get(i).getHtmlURL());
        ((TextView)view.findViewById(R.id.language)).setText(repositoryList.get(i).getLanguage());
        ((TextView)view.findViewById(R.id.description)).setText(repositoryList.get(i).getDescription());
        return view;
    }
}
