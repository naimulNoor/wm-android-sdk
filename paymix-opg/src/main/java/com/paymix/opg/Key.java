package com.paymix.opg;

public enum Key {
    keep_me_logged_in,
    access_token,
    user_name,
    about_us, about_us_image_link,
    user_profile,
    user_menus,
    user_role,
    transaction_info,

    user_profile_pic_path,
    transaction_pin,
    full_name,
    email,
    current_balance, is_drawer_opened,
    live_app_version, current_app_version, verification_title, verification_message,

    wmx_id, wmx_api_key, wmx_merchant_user_name, wmx_merchant_pass,  base_url,


    is_changing_password, device_token,is_device_token_stored, seen_notification_list, un_seen_notification_count,

    // History
    history_name, topup_history, fund_history, commission_history, support_history, support_id,

    // Start Activity for result
    data, district, profession, result,

    Topup, Offers, Profile,

    APP_LANGUAGE,

    offer_amount, topup_quick_amounts, add_fund_quick_amounts, promotional_banner_url, offer_banners_map;

    public enum ProfileKey{
        u_first_name, u_last_name, u_username, u_full_name,  u_date_of_birth, u_gender,  u_blood_group ,u_email, u_contact,
        u_profession, u_district, u_profile_pic_path, u_slug, u_role, u_is_robi, u_ref_number,
        u_ref_id, account_created_at, is_verified
    }

    public enum Menu{
        facebook_login, display_social_page, buy_easyload, recharge_create, easyload_history, commission_history,
        support_history, recharge_list, add_support, settings, about_us, change_password, log_out
    }

    public enum Fragment{
        fragment, settings, about_us, system_upgrade, internet_connectivity, app_update_fragment
    }

    public enum TransactionInfo{
         wmx_id, marchant_ref_id, marchant_order_id, amount, customer_email
    }

    public enum ImageKey{

        CAPTURED_IMAGE_PATH
    }
}