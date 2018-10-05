package com.bridgellc.bridge.entity;

import java.io.Serializable;


public class HomeSingleItemEntity implements Serializable {


    private String item_id;
    private String user_id;
    private String item_mode;
    private String item_type;
    private String delivery_type;
    private String item_category;
    private String item_name;
    private String item_quantity;
    private String item_cost;
    private String negotiate_cost;
    private String item_condition;
    private String is_fixed_cost;
    private String item_description;
    private String picture1;
    private String picture2;
    private String picture3;
    private String is_big_bubble;
    private String is_billboard_bubble;
    private String billboard_bubble_hours;
    private String file_url;
    private String file_url_type;
    private String created_datetime;
    private String modified_datetime;
    private String status_datetime;
    private String delete_flag;
    private String status;
    private String user_first_name;
    private String user_last_name;
    private String user_rating;
    private String category_name;
    private String owner_first_name;
    private String user_verified;
    private String buyer_verified;
    private String owner_verified;
    private String owner_rating;
    private String owner_last_name;
    private String buyer_id;
    private String buyer_first_name;
    private String partner;
    private String tips;
    private String Headerakey;
    private String headId;
    private String buyer_university_name;
    private String owner_university_name;
    private String buyer_last_name;
    private String buyer_rating;
    private String payment_id;
    private String amount_received;
    private String processing_fee;
    private String quantity;
    private String purchase_quantity;
    private String finish_service;
    private String file_status;
    private String is_approve;
    private String bid_id;
    private String to_user_id;
    private String bid_amount;
    private String date_time;
    private String approve;
    private String chat_count;
    private String preview_url;
    private String original_url;
    private String complete;
    private String notification_id;
    private String apifrom;
    private String typeid;
    private String message;
    private String sender_first_name;
    private String sender_last_name;
    private String venue;
    private String event_date_time;
    private String item_buyer_rating;
    private String item_buyer_comments;
    private String item_seller_rating;
    private String item_seller_comments;
    private String notifystatus;
    private String payment_mode;
    private String buyer_partner;
    private String seller_partner;
    private String phone_number;
    private String website;
    private String negotiate_offers;

    private String owner_email;

    public String getOwner_email() {
        return owner_email;
    }

    public void setOwner_email(String owner_email) {
        this.owner_email = owner_email;
    }

    public String getNegotiate_offers() {
        return negotiate_offers;
    }

    public void setNegotiate_offers(String negotiate_offers) {
        this.negotiate_offers = negotiate_offers;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }


    public String getSeller_partner() {
        return seller_partner;
    }

    public void setSeller_partner(String seller_partner) {
        this.seller_partner = seller_partner;
    }

    public String getBuyer_partner() {
        return buyer_partner;
    }

    public void setBuyer_partner(String buyer_partner) {
        this.buyer_partner = buyer_partner;
    }


    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }


    public String getHeaderakey() {
        return Headerakey;
    }

    public void setHeaderakey(String headerakey) {
        Headerakey = headerakey;
    }

    public String getHeadId() {
        return headId;
    }

    public void setHeadId(String headId) {
        this.headId = headId;
    }


    public String getOwner_university_name() {
        return owner_university_name;
    }

    public void setOwner_university_name(String owner_university_name) {
        this.owner_university_name = owner_university_name;
    }

    public String getBuyer_university_name() {
        return buyer_university_name;
    }

    public void setBuyer_university_name(String buyer_university_name) {
        this.buyer_university_name = buyer_university_name;
    }

    public String getTotal_cost() {
        return total_cost;
    }

    public void setTotal_cost(String total_cost) {
        this.total_cost = total_cost;
    }

    private String total_cost;


    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }


    public String getNotifystatus() {
        return notifystatus;
    }

    public void setNotifystatus(String notifystatus) {
        this.notifystatus = notifystatus;
    }

    public String getItem_seller_comments() {
        return item_seller_comments;
    }

    public void setItem_seller_comments(String item_seller_comments) {
        this.item_seller_comments = item_seller_comments;
    }

    public String getItem_seller_rating() {
        return item_seller_rating;
    }

    public void setItem_seller_rating(String item_seller_rating) {
        this.item_seller_rating = item_seller_rating;
    }

    public String getItem_buyer_comments() {
        return item_buyer_comments;
    }

    public void setItem_buyer_comments(String item_buyer_comments) {
        this.item_buyer_comments = item_buyer_comments;
    }

    public String getItem_buyer_rating() {
        return item_buyer_rating;
    }

    public void setItem_buyer_rating(String item_buyer_rating) {
        this.item_buyer_rating = item_buyer_rating;
    }


    public String getEvent_date_time() {
        return event_date_time;
    }

    public void setEvent_date_time(String event_date_time) {
        this.event_date_time = event_date_time;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }


    public String getSender_last_name() {
        return sender_last_name;
    }

    public void setSender_last_name(String sender_last_name) {
        this.sender_last_name = sender_last_name;
    }

    public String getSender_first_name() {
        return sender_first_name;
    }

    public void setSender_first_name(String sender_first_name) {
        this.sender_first_name = sender_first_name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getApifrom() {
        return apifrom;
    }

    public void setApifrom(String apifrom) {
        this.apifrom = apifrom;
    }

    public String getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(String notification_id) {
        this.notification_id = notification_id;
    }

    public String getComplete() {
        return complete;
    }

    public void setComplete(String complete) {
        this.complete = complete;
    }

    public String getOriginal_url() {
        return original_url;
    }

    public void setOriginal_url(String original_url) {
        this.original_url = original_url;
    }

    public String getPreview_url() {
        return preview_url;
    }

    public void setPreview_url(String preview_url) {
        this.preview_url = preview_url;
    }


    public String getChat_count() {
        return chat_count;
    }

    public void setChat_count(String chat_count) {
        this.chat_count = chat_count;
    }


    public String getApprove() {
        return approve;
    }

    public void setApprove(String approve) {
        this.approve = approve;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getBid_amount() {
        return bid_amount;
    }

    public void setBid_amount(String bid_amount) {
        this.bid_amount = bid_amount;
    }

    public String getTo_user_id() {
        return to_user_id;
    }

    public void setTo_user_id(String to_user_id) {
        this.to_user_id = to_user_id;
    }

    public String getBid_id() {
        return bid_id;
    }

    public void setBid_id(String bid_id) {
        this.bid_id = bid_id;
    }

    public String getIs_approve() {
        return is_approve;
    }

    public void setIs_approve(String is_approve) {
        this.is_approve = is_approve;
    }


    public String getFile_status() {
        return file_status;
    }

    public void setFile_status(String file_status) {
        this.file_status = file_status;
    }

    public String getFinish_service() {
        return finish_service;
    }

    public void setFinish_service(String finish_service) {
        this.finish_service = finish_service;
    }


    public String getPurchase_quantity() {
        return purchase_quantity;
    }

    public void setPurchase_quantity(String purchase_quantity) {
        this.purchase_quantity = purchase_quantity;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public String getAmount_received() {
        return amount_received;
    }

    public void setAmount_received(String amount_received) {
        this.amount_received = amount_received;
    }

    public String getProcessing_fee() {
        return processing_fee;
    }

    public void setProcessing_fee(String processing_fee) {
        this.processing_fee = processing_fee;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getBuyer_id() {
        return buyer_id;
    }

    public void setBuyer_id(String buyer_id) {
        this.buyer_id = buyer_id;
    }


    public String getOwner_verified() {
        return owner_verified;
    }

    public void setOwner_verified(String owner_verified) {
        this.owner_verified = owner_verified;
    }

    public String getBuyer_verified() {
        return buyer_verified;
    }

    public void setBuyer_verified(String buyer_verified) {
        this.buyer_verified = buyer_verified;
    }

    public String getUser_verified() {
        return user_verified;
    }

    public void setUser_verified(String user_verified) {
        this.user_verified = user_verified;
    }

    public String getBuyer_first_name() {
        return buyer_first_name;
    }

    public void setBuyer_first_name(String buyer_first_name) {
        this.buyer_first_name = buyer_first_name;
    }

    public String getBuyer_last_name() {
        return buyer_last_name;
    }

    public void setBuyer_last_name(String buyer_last_name) {
        this.buyer_last_name = buyer_last_name;
    }

    public String getBuyer_rating() {
        return buyer_rating;
    }

    public void setBuyer_rating(String buyer_rating) {
        this.buyer_rating = buyer_rating;
    }


    public String getOwner_last_name() {
        return owner_last_name;
    }

    public void setOwner_last_name(String owner_last_name) {
        this.owner_last_name = owner_last_name;
    }

    public String getOwner_first_name() {
        return owner_first_name;
    }

    public void setOwner_first_name(String owner_first_name) {
        this.owner_first_name = owner_first_name;
    }

    public String getOwner_rating() {
        return owner_rating;
    }

    public void setOwner_rating(String owner_rating) {
        this.owner_rating = owner_rating;
    }


    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getItem_mode() {
        return item_mode;
    }

    public void setItem_mode(String item_mode) {
        this.item_mode = item_mode;
    }

    public String getItem_type() {
        return item_type;
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }

    public String getDelivery_type() {
        return delivery_type;
    }

    public void setDelivery_type(String delivery_type) {
        this.delivery_type = delivery_type;
    }

    public String getItem_category() {
        return item_category;
    }

    public void setItem_category(String item_category) {
        this.item_category = item_category;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_quantity() {
        return item_quantity;
    }

    public void setItem_quantity(String item_quantity) {
        this.item_quantity = item_quantity;
    }

    public String getItem_cost() {
        return item_cost;
    }

    public void setItem_cost(String item_cost) {
        this.item_cost = item_cost;
    }

    public String getNegotiate_cost() {
        return negotiate_cost;
    }

    public void setNegotiate_cost(String negotiate_cost) {
        this.negotiate_cost = negotiate_cost;
    }

    public String getItem_condition() {
        return item_condition;
    }

    public void setItem_condition(String item_condition) {
        this.item_condition = item_condition;
    }

    public String getIs_fixed_cost() {
        return is_fixed_cost;
    }

    public void setIs_fixed_cost(String is_fixed_cost) {
        this.is_fixed_cost = is_fixed_cost;
    }

    public String getItem_description() {
        return item_description;
    }

    public void setItem_description(String item_description) {
        this.item_description = item_description;
    }

    public String getPicture1() {
        return picture1;
    }

    public void setPicture1(String picture1) {
        this.picture1 = picture1;
    }

    public String getPicture2() {
        return picture2;
    }

    public void setPicture2(String picture2) {
        this.picture2 = picture2;
    }

    public String getPicture3() {
        return picture3;
    }

    public void setPicture3(String picture3) {
        this.picture3 = picture3;
    }

    public String getIs_big_bubble() {
        return is_big_bubble;
    }

    public void setIs_big_bubble(String is_big_bubble) {
        this.is_big_bubble = is_big_bubble;
    }

    public String getIs_billboard_bubble() {
        return is_billboard_bubble;
    }

    public void setIs_billboard_bubble(String is_billboard_bubble) {
        this.is_billboard_bubble = is_billboard_bubble;
    }

    public String getBillboard_bubble_hours() {
        return billboard_bubble_hours;
    }

    public void setBillboard_bubble_hours(String billboard_bubble_hours) {
        this.billboard_bubble_hours = billboard_bubble_hours;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public String getFile_url_type() {
        return file_url_type;
    }

    public void setFile_url_type(String file_url_type) {
        this.file_url_type = file_url_type;
    }

    public String getCreated_datetime() {
        return created_datetime;
    }

    public void setCreated_datetime(String created_datetime) {
        this.created_datetime = created_datetime;
    }

    public String getModified_datetime() {
        return modified_datetime;
    }

    public void setModified_datetime(String modified_datetime) {
        this.modified_datetime = modified_datetime;
    }

    public String getStatus_datetime() {
        return status_datetime;
    }

    public void setStatus_datetime(String status_datetime) {
        this.status_datetime = status_datetime;
    }

    public String getDelete_flag() {
        return delete_flag;
    }

    public void setDelete_flag(String delete_flag) {
        this.delete_flag = delete_flag;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser_first_name() {
        return user_first_name;
    }

    public void setUser_first_name(String user_first_name) {
        this.user_first_name = user_first_name;
    }

    public String getUser_last_name() {
        return user_last_name;
    }

    public void setUser_last_name(String user_last_name) {
        this.user_last_name = user_last_name;
    }

    public String getUser_rating() {
        return user_rating;
    }

    public void setUser_rating(String user_rating) {
        this.user_rating = user_rating;
    }
}
