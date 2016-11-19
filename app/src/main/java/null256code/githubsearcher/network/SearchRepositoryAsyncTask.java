package null256code.githubsearcher.network;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import null256code.githubsearcher.R;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * GitHubのAPIサーバからリポジトリ情報を検索する。
 * Created by kanto on 2016/11/18.
 */

public class SearchRepositoryAsyncTask extends AsyncTask<Integer, Integer, String> {

    private static final String API_SERVER_URL = "https://api.github.com/";
    private static final String SEARCH_REPOSITORY_URL = API_SERVER_URL + "search/repositories";

    private HttpUrl.Builder urlBuilder = HttpUrl.parse(SEARCH_REPOSITORY_URL).newBuilder();
    private Activity activity;

    public SearchRepositoryAsyncTask(Activity activity, String keyword) {
        this.activity = activity;
        urlBuilder.addQueryParameter("q", keyword);
    }
    public SearchRepositoryAsyncTask(Activity activity, String keyword, String sort) {
        this(activity, keyword, sort, "desc");
    }
    public SearchRepositoryAsyncTask(Activity activity, String keyword, String sort, String order) {
        this(activity, keyword);
        urlBuilder.addQueryParameter("sort", sort).addQueryParameter("order", order);
    }

    @Override
    protected String doInBackground(Integer... params) {
        HttpUrl url = urlBuilder.build();
        Request request = new Request.Builder().url(url).get().build();

        String result = null;
        OkHttpClient client = new OkHttpClient(); //TODO タイムアウト等の設定？
        try {
            Response response = client.newCall(request).execute();
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        TextView resultText = (TextView)activity.findViewById(R.id.result);
        resultText.setText(result);
//        try {
//            JSONArray array = new JSONArray(result);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }
}
