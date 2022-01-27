package com.dreamstudio.qqlogintest

data class QQLogin(val ret:Int,val openid:String,val access_token:String,
                   val pay_token:String,val expires_in:Int,val pf:String,
                   val pfkey:String,val msg:String,val login_cost:Int,val query_authority_cost:Int,
                   val authority_cost:Int,val expires_time:Long)
data class QQInfo(val ret:Int,val msg:String,val is_lost:Int,val nickname:String,
                  val gender:String,val gender_type:Int,val province:String,val city:String,
                  val year:String,val constellation:String,val figureurl:String,
                  val figureurl_1:String,val figureurl_2:String,val figureurl_qq_1:String,
                  val figureurl_qq_2:String,val figureurl_qq:String,val figureurl_type:String,
                  val is_yellow_vip:String,val vip:String,val yellow_vip_level:String,val level:String,
                  val is_yellow_year_vip:String)