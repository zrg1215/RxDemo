package andr.wordoor.com.rxjavademo.entity;

import java.util.List;

/**
 * Created by zrg on 2017/6/27.
 */

public class AndroidResponse extends BaseBean{
    public boolean error;
    public List<Result> results;

    public class Result {
        public String id;
        public String createdAt;
        public String desc;
        public List<String> images;
        public String publishedAt;
        public String source;
        public String type;
        public String url;
        public boolean used;
        public String who;
    }
}
