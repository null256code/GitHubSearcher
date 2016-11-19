package null256code.githubsearcher.network;

import android.os.AsyncTask;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * GitHubのAPIサーバからリポジトリ情報を検索する。
 * Created by kanto on 2016/11/18.
 */

public class SearchRepositoryAsyncTask extends AsyncTask<String, Integer, JSONObject> {

    private static final String API_SERVER_URL = "https://api.github.com/";
    private static final String SEARCH_REPOSITORY_URL = API_SERVER_URL + "search/repositories";
    private HttpUrl.Builder urlBuilder = HttpUrl.parse(SEARCH_REPOSITORY_URL).newBuilder();

    public static final String JSON_KEY_INCOMPLETE_RESULT = "incomplete_results";
    public static final String JSON_KEY_TOTAL_COUNT = "total_count";
    public static final String JSON_KEY_ITEMS = "items";
    public static final String JSON_KEY_ID = "id";
    public static final String JSON_KEY_OWNER = "owner";
    public static final String JSON_KEY_OWNER_LOGIN = "login";
    public static final String JSON_KEY_OWNER_AVATAR = "avatar_url";
    public static final String JSON_KEY_HTML_URL = "html_url";
    public static final String JSON_KEY_DESCRIPTION = "description";
    public static final String JSON_KEY_LANGUAGE = "language";

    public interface AsyncCallback {
        void preExecute();
        void postExecute(JSONObject result);
        void progressUpdate(int progress);
        void cancel();
    }
    private AsyncCallback mAsyncCallback = null;
    public SearchRepositoryAsyncTask(AsyncCallback asyncCallback) {
        this.mAsyncCallback = asyncCallback;
    }

    /** 検索条件をセットする。Nullか空文字のものはセットしない。 */
    public void updateSearchCondition(String keyword, String sort, String order) {
        urlBuilder.addQueryParameter("q", keyword);
        if(StringUtils.isNotBlank(sort)) {
            urlBuilder.addQueryParameter("sort", sort);
            if(StringUtils.isNotBlank(order)) {
                urlBuilder.addQueryParameter("order", order);
            }
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mAsyncCallback.preExecute();
    }
    @Override
    protected void onProgressUpdate(Integer... _progress) {
        super.onProgressUpdate(_progress);
        mAsyncCallback.progressUpdate(_progress[0]);
    }
    @Override
    protected void onPostExecute(JSONObject _result) {
        super.onPostExecute(_result);
        mAsyncCallback.postExecute(_result);
    }
    @Override
    protected void onCancelled() {
        super.onCancelled();
        mAsyncCallback.cancel();
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        HttpUrl url = urlBuilder.build();
        Request request = new Request.Builder().url(url).get().build();

        OkHttpClient client = new OkHttpClient(); //TODO タイムアウト等の設定？
        try {
            //TODO ネット繋がんないとき
            Response response = client.newCall(request).execute();
            return new JSONObject(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
