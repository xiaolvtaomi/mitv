package com.yealink.sample;

import android.content.Context;
import android.content.res.Resources;

import com.tv.ui.metro.R;
import com.yealink.lib.common.wrapper.AccountConstant;
import com.yealink.lib.common.wrapper.CallSession;


/**
 * Created by chenhn on 2017/3/20.
 */

public class CallErrorCodeUtil {

    private static final long TIMEOUT = 1000;

    public static final int TYPE_BY_ME = 0;
    public static final int TYPE_BY_HIM = 1;
    public static final int TYPE_FAILED = 2;
    public static final int TYPE_MAX = 3;

    private static final String KEY_HANGUP_TYPE = "KEY_HANGUP_TYPE";
    private static final String KEY_DURATION = "KEY_DURATION";
    private static final String KEY_ERROR_CODE = "KEY_ERROR_CODE";
    private static final String KEY_EXTRA_CODE = "KEY_EXTRA_CODE";
    private static final String KEY_PROTOCOL = "KEY_PROTOCOL";

    private int mHangupType;
    private int mErrorCode;
    private int mExtraCode;
    private int mProtocol = AccountConstant.PROTOCOL_SIP;
    private int mDuration;

    private Context mContext;
    private Resources mResources;

    public CallErrorCodeUtil(Context context) {
        mContext = context;
        mResources = context.getResources();
    }

    public void setHangupType(int mHangupType) {
        if(mHangupType >= TYPE_MAX || mHangupType < 0){
            throw new IllegalArgumentException("type >= TYPE_MAX || type < 0");
        }
        this.mHangupType = mHangupType;
    }

    public void setErrorCode(int mErrorCode) {
        this.mErrorCode = mErrorCode;
    }

    public void setExtraCode(int mExtraCode) {
        this.mExtraCode = mExtraCode;
    }

    public void setProtocol(int mProtocol) {
        this.mProtocol = mProtocol;
    }

    public void setDuration(int mDuration) {
        this.mDuration = mDuration;
    }

    public String getString(){
        StringBuilder sb = new StringBuilder();
        switch (mHangupType) {
            case TYPE_BY_HIM:
                sb.append(mResources.getString(R.string.hangup_by_him));
                break;
            case TYPE_BY_ME:
                sb.append(mResources.getString(R.string.hangup_by_me));
                break;
            case TYPE_FAILED:
//                String str = mResources.getString(R.string.call_failed);
//                sb.append( str + "," + getExtraError());
                sb.append(mResources.getString(R.string.error_cur_talking));

                break;
        }
//        if(mDuration > 0){
//            sb.append("\n");
//            sb.append(getDurationText());
//        }
        return sb.toString();
    }
    
    private String getErrorText(){
        switch (mErrorCode) {
            case CallSession.REASON_FAILED:
                return getExtraError();
            case CallSession.REASON_NO_USABLE_ACCOUNT:
                return mResources.getString(R.string.reason_no_usable_account);
            case CallSession.REASON_REFUSED:
            case CallSession.REASON_CANCELED:
                return mResources.getString(R.string.hangup_by_me);
            case CallSession.REASON_TIMEOUTED:
                return mResources.getString(R.string.reason_timeout);
            case CallSession.REASON_UNKNOW_PROTOCOL:
                return mResources.getString(R.string.reason_unknown_protocol);
        }
        return mResources.getString(R.string.unknown_error);
    }

    private String getExtraError(){
        if(mProtocol == AccountConstant.PROTOCOL_H323){
            switch (mExtraCode) {
                case CallSession.H323_END_BY_ANSWER_DENIED:
                    return mResources.getString(R.string.h323_end_by_answer_denied);
                case CallSession.H323_END_BY_CALL_FORWARD:
                    return mResources.getString(R.string.h323_end_by_call_forward);
                case CallSession.H323_END_BY_CALLER_ABORT:
                    return mResources.getString(R.string.h323_end_by_caller_abort);
                case CallSession.H323_END_BY_CAPABILITY_EXCHANGE:
                    return mResources.getString(R.string.h323_end_by_capability_exchange);
                case CallSession.H323_END_BY_CONNECT_FAIL:
                    return mResources.getString(R.string.h323_end_by_connect_fail);
                case CallSession.H323_END_BY_DURATION_LIMIT:
                    return mResources.getString(R.string.h323_end_by_duration_limit);
                case CallSession.H323_END_BY_GATE_KEEPER:
                    return mResources.getString(R.string.h323_end_by_gatekeeper);
                case CallSession.H323_END_BY_HOST_OFFLINE:
                    return mResources.getString(R.string.h323_end_by_host_offline);
                case CallSession.H323_END_BY_INVALID_CONFERENCE_ID:
                    return mResources.getString(R.string.h323_end_by_invalid_conference_id);
                case CallSession.H323_END_BY_INVALID_NUMBER_FORMAT:
                    return mResources.getString(R.string.h323_end_by_invalid_number_format);
                case CallSession.H323_END_BY_LOCAL_BUSY:
                    return mResources.getString(R.string.h323_end_by_local_busy);
                case CallSession.H323_END_BY_LOCAL_CONGESTION:
                    return mResources.getString(R.string.h323_end_by_local_congestion);
                case CallSession.H323_END_BY_LOCAL_USER:
                    return mResources.getString(R.string.h323_end_by_local_user);
                case CallSession.H323_END_BY_NO_ACCEPT:
                    return mResources.getString(R.string.h323_end_by_no_accept);
                case CallSession.H323_END_BY_NO_ANWSWER:
                    return mResources.getString(R.string.h323_end_by_no_anwswer);
                case CallSession.H323_END_BY_NO_BANDWIDTH:
                    return mResources.getString(R.string.h323_end_by_no_bandwidth);
                case CallSession.H323_END_BY_NO_ENDPOINT:
                    return mResources.getString(R.string.h323_end_by_no_endpoint);
                case CallSession.H323_END_BY_NO_FEATURE_SUPPORT:
                    return mResources.getString(R.string.h323_end_by_no_feature_support);
                case CallSession.H323_END_BY_NO_USER:
                    return mResources.getString(R.string.h323_end_by_no_user);
                case CallSession.H323_END_BY_OSP_REFUSAL:
                    return mResources.getString(R.string.h323_end_by_osp_refusal);
                case CallSession.H323_END_BY_PARAM_ERROR:
                    return mResources.getString(R.string.h323_end_by_param_error);
                case CallSession.H323_END_BY_Q931_CAUSE:
                    return mResources.getString(R.string.h323_end_by_q931_cause);
                case CallSession.H323_END_BY_REFUSAL:
                    return mResources.getString(R.string.h323_end_by_refusal);
                case CallSession.H323_END_BY_REMOTE_BUSY:
                    return mResources.getString(R.string.h323_end_by_remote_busy);
                case CallSession.H323_END_BY_REMOTE_CONGESTION:
                    return mResources.getString(R.string.h323_end_by_remote_congestion);
                case CallSession.H323_END_BY_REMOTE_USER:
                    return mResources.getString(R.string.h323_end_by_remote_user);
                case CallSession.H323_END_BY_SECURITY_DENIAL:
                    return mResources.getString(R.string.h323_end_by_security_denial);
                case CallSession.H323_END_BY_TEMPORARY_FAILURE:
                    return mResources.getString(R.string.h323_end_by_temporary_failure);
                case CallSession.H323_END_BY_TRANSPORT_FAIL:
                    return mResources.getString(R.string.h323_end_by_transport_fail);
                case CallSession.H323_END_BY_UNREACHABLE:
                    return mResources.getString(R.string.h323_end_by_unreachable);
                case CallSession.H323_END_BY_UNSPECIFIED_PORTOCOL:
                    return mResources.getString(R.string.h323_end_by_unspecified_protocol);
                case CallSession.NotifyNothing:
                    return "";
                default:
                    return mResources.getString(R.string.unknown_error);
            }
        } else if(mProtocol == AccountConstant.PROTOCOL_SIP){
            switch (mExtraCode) {
                case CallSession.VoipSIPEndedByAnonymityDisallowed:
                    return mResources.getString(R.string.sip_end_by_anonymity_disallowed);
                case CallSession.VoipSIPEndedByBadEvent:
                    return mResources.getString(R.string.sip_end_by_bad_event);
                case CallSession.VoipSIPEndedByBadExtension:
                    return mResources.getString(R.string.sip_end_by_bad_extension);
                case CallSession.VoipSIPEndedByBadRequest:
                    return mResources.getString(R.string.sip_end_by_bad_request);
                case CallSession.VoipSIPEndedByBusyHere:
                    return mResources.getString(R.string.sip_end_by_busy_here);
                case CallSession.VoipSIPEndedByCallTransactionDoesNotExist:
                    return mResources.getString(R.string.sip_end_by_call_transaction_not_exist);
                case CallSession.VoipSIPEndedByForbidden:
                    return mResources.getString(R.string.sip_end_by_forbidden);
                case CallSession.VoipSIPEndedByInternalServerError:
                    return mResources.getString(R.string.sip_end_by_internal_server_error);
                case CallSession.VoipSIPEndedByLoopDetected:
                    return mResources.getString(R.string.sip_end_by_loop_detected);
                case CallSession.VoipSIPEndedByMethodNotAllowed:
                    return mResources.getString(R.string.sip_end_by_method_not_allowed);
                case CallSession.VoipSIPEndedByNotAcceptable:
                    return mResources.getString(R.string.sip_end_by_not_acceptable);
                case CallSession.VoipSIPEndedByNotAcceptableHere:
                    return mResources.getString(R.string.sip_end_by_not_acceptable_here);
                case CallSession.VoipSIPEndedByNotFound:
                    return mResources.getString(R.string.sip_end_by_not_found);
                case CallSession.VoipSIPEndedByRequestPending:
                    return mResources.getString(R.string.sip_end_by_request_pending);
                case CallSession.VoipSIPEndedByRequestTimeOut:
                    return mResources.getString(R.string.sip_end_by_request_timeout);
                case CallSession.VoipSIPEndedByServiceLost:
                    return mResources.getString(R.string.sip_end_by_service_lost);
                case CallSession.VoipSIPEndedByServiceUnavailable:
                    return mResources.getString(R.string.sip_end_by_service_unavailable);
                case CallSession.VoipSIPEndedByTemporarilyUnavailable:
                    return mResources.getString(R.string.sip_end_by_temporarily_unavailable);
                case CallSession.VoipSIPEndedByUnknownUriScheme:
                    return mResources.getString(R.string.sip_end_by_unknown_uri_scheme);
                case CallSession.VoipSIPEndedByUnsupportedMediaType:
                    return mResources.getString(R.string.sip_end_by_unsupport_media_type);
                case CallSession.VoipSIPEndedByUnsupportedUriScheme:
                    return mResources.getString(R.string.sip_end_by_unsupport_uri_scheme);
                case CallSession.VoipSIPEndedByDecline:
                    return mResources.getString(R.string.sip_end_by_decline);
                case CallSession.NotifyNothing:
                    return "";
                default:
                    return mResources.getString(R.string.unknown_error);
            }
        }
        return "";
    }
}
