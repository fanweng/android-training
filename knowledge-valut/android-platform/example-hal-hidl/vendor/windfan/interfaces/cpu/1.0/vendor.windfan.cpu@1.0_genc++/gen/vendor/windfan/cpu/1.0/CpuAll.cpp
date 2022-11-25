::android::hardware::Return<bool> BpHwCpu::_hidl_setScalingGovernor(
    // BpHwCpu inherits from IInterface
    ::android::hardware::IInterface *_hidl_this,
    // used for instrumentation (debug only)
    ::android::hardware::details::HidlInstrumentor *_hidl_this_instrumentor,
    // first input (governor)
    const ::android::hardware::hidl_string& governor) {
//... ...
}



const char* ICpu::descriptor("vendor.windfan.cpu@1.0::ICpu");

::android::hardware::Parcel _hidl_data;
::android::hardware::Parcel _hidl_reply; // reply buffer
::android::status_t _hidl_err;

// the 1st object written to the buffer is the fully qualified interface name
_hidl_err = _hidl_data.writeInterfaceToken(BpHwCpu::descriptor);
// the 2nd object is the governor string
_hidl_err = _hidl_data.writeBuffer(&governor, sizeof(governor), &_hidl_governor_parent);



_hidl_transact_err = ::android::hardware::IInterface::asBinder(_hidl_this)->transact(
    // function index, getScalingGovernor is 1
    2 /* setScalingGovernor */,
    // Parcel object that will be copied to the service via the binder
    _hidl_data,
    // empty Parcel object to be filled by the binder from service reply
    &_hidl_reply,
    0 /* flags */);



::android::status_t BnHwCpu::_hidl_setScalingGovernor(
    ::android::hidl::base::V1_0::BnHwBase* _hidl_this,
    const ::android::hardware::Parcel &_hidl_data,
    ::android::hardware::Parcel *_hidl_reply,
    // the callback method to send back in the _hild_reply
    TransactCallback _hidl_cb) {
//... ...
}