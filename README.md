# GitHubSearcher
GitHub/repository search APIの練習アプリ  
(https://developer.github.com/v3/search/#search-repositories)  
週末の金～日ぐらいで作りました。

▼APK  
<https://github.com/null256code/GitHubSearcher/blob/master/doc/GitHubSearcher-debug.apk>

▼画面のキャプチャ  
<img src="https://raw.githubusercontent.com/null256code/GitHubSearcher/master/doc/capture.png" height="400" />  

▼使用した外部ライブラリ  
・OkHttp ・Picasso ・Apache Commons  

▼機能  
・キーボードが文字入力確定すると、リポジトリを検索して結果を表示する。  
・検索ボタンを押してももちろん検索する。  
・出た検索結果をタップすると、そのリポジトリのページをブラウザで見ることが出来る。  

▼課題/反省  
・ListViewまわりはFragmentで実装すべきだった？  
 Fragmentまわりの知識があまりなくちゃんと調べれなかった。恥ずかしいし、一番の課題か。  
・勢いで作ったので変数の命名に規則性が無いところがある。  
・ハンドリングが甘い。ネット繋いでいないとき、リクエスト何回も飛ばしたときなど。  
・見た目が貧弱。レイアウトもだし、リストが変わらないとロードが完了したのかが分からない。

▼感想  
Android思った以上に分かってなかったな。。となって少し凹みました、が  
久々にAndroid触ったのでそこは楽しかったです。  
最近Kotlinとかで組むのも流行ってるらしいので、今度やってみたいですね。  
