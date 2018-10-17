package com.gtv.hanhee.novelreading.Model;


import com.gtv.hanhee.novelreading.Model.Base.Base;

import java.util.List;


public class HotWord extends Base {
    public List<String> hotWords;

    public static class combineHotWord {
        public String transHotWord;
        public String hotWord;

        public combineHotWord(String transHotWord, String hotWord) {
            this.transHotWord = transHotWord;
            this.hotWord = hotWord;
        }

        public String getTransHotWord() {
            return transHotWord;
        }

        public void setTransHotWord(String transHotWord) {
            this.transHotWord = transHotWord;
        }

        public String getHotWord() {
            return hotWord;
        }

        public void setHotWord(String hotWord) {
            this.hotWord = hotWord;
        }
    }
}
