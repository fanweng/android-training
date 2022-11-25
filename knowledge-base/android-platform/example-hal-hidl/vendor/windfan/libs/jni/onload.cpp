namespace cpu {
    int register_windfan_Cpu(JNIEnv* env);
};

using namespace cpu;

extern "C" jint JNI_OnLoad(JavaVM* vm, void*)
{
    JNIEnv* env = NULL;
    jint result = -1;

    if (vm->GetEnv((void**) &env, JNI_VERSION_1_4) != JNI_OK) {
        ALOGE("GetEnv failed!");
        return result;
    }
    ALOG_ASSERT(env, "Could not retrieve the env!");

    cpu::register_windfan_Cpu(env);
    return JNI_VERSION_1_4;
}
