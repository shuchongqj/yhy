package com.yhy.network.resp.snscenter

import com.yhy.network.resp.PageResp

class GetTopicPageListResp : PageResp() {

    var topicInfoResultList : List<TopicInfoResult>? = null

    companion object {
        class TopicInfoResult {
            var	title : String = ""	// 话题名称
            var 	topicId : Long = 0	// 话题id
            var 	content : String = ""	// 话题内容
            var 	pics : String = ""	// 话题图片
            var 	redNum : Long = 0		// 阅读数量
            var 	talkNum : Long = 0	// 参与数量
        }
    }
}