package com.walletmix.paymixbusiness.utils

/**
 * Created on 09/07/2019.
 */
object AppConstants {
    const val LAUNCH_CAMERA = 101
    const val LAUNCH_GALLERY = 102
    const val LAUNCH_PDF = 12
    const val LAUNCH_SELECT_REGION_ACTIVITY: Int = 103
    const val LAUNCH_CHANGE_REWARD_TYPE_ACTIVITY: Int = 105
    const val LAUNCH_MFS_ACTIVITY: Int = 103

    const val REQUEST_CAMERA = 201
    const val REQUEST_GALLERY = 202
    const val REQUEST_CONTACT = 127
    const val REQUEST_STORAGE = 128



    const val REQUEST_READ_SMS = 301
    const val REQUEST_RECIVE_SMS = 302


    const val REQUEST_NOTIFICATION = 401

}

object OtpVerificationType {
    const val ADD_ACCOUNT = 1
    const val CHANGE_PIN = 2
    const val REGULAR = 0
}


object SOURCE_TYPE {
    const val ADD_FUND = 1
    const val ADD_FUND_COMMISSION = 2
    const val POINT_CONVERSION = 3
    const val CAMPAIGN = 4
    const val FTR_COMMISSION = 5
    const val GSTORE_COMMISSION = 6
}


object PaymentMethod {
    const val bKash = 1
    const val Nagad = 2
    const val SSL = 3
}

object PaymentMethodName {
    const val bKash = "bKash"
    const val Nagad = "Nagad"
    const val SSL = "ssl"
}

object ConnectionType {
    const val PREPAID = 0
    const val POSTPAID = 1
}


object OfferType {
    const val BUNDLE = "0"
    const val INTERNET = "1"
    const val VOICE = "2"
    const val VAS = "3"
}

object VasType {
    const val SOCIAL_AND_CHAT = "0"
    const val MOBILE_ASSISTANT = "1"
    const val MUSIC_AND_ENTERTAINMENT = "2"
    const val NEWS_AND_INFO = "3"
    const val LIFE_STYLE = "4"
}

object MfsStatus {
    const val SUCCESS = "success"
    const val FAILED = "failed"
    const val CANCELLED = "cancelled"
    const val CUSTOM = "custom"
}


object RechargeType {
    const val BUNDLE = 0
    const val INTERNET = 1
    const val VOICE = 2
    const val VAS = 3
    const val EASY_LOAD = 4
    const val GSTORE_RECHARGE = 11

    //roni's code
    const val GA = 5
    const val MNP = 6
    const val USIM_Swap = 7
    const val SIM_REPLACEMENT = 8
    const val OWNERSHIP_TRANSFER = 9
    const val PREPAID_TO_POSTPAID_MIGRATION = 10








}

object RewardType {
    const val COMMISSION = 0
    const val POINT = 1
}

object HistoryType {
    const val CURRENT_MONTH = "1"
    const val LAST_30_DAYS = "2"
}

object BannerType {
    const val REFERRAL = "1"
    const val CAMPAIGN = "2"
    const val OTHERS = "3"
    const val VIDEO_LINK = "4"
    const val PROMOTIONAL = "1"
}

object GiftType {
    const val DATA_PACK = 1
    const val GIFT_ITEM = 2
}

object Brand {
    const val ROBI = 8
    const val AIRTEL = 6
}

object ConsumptionType {
    const val POINT_TO_GIFT = 1
    const val POINT_TO_DATA_PACK = 2
    const val POINT_CONVERSION = 3
}

object MyGiftStatus {
    const val PURCHASED = 3
    const val COURIERED = 4
    const val DELIVERED = 5
}

object SingleAppLink {
    const val robi = "net.omobio.robisc"
    const val airtel = "net.omobio.airtelsc"

}