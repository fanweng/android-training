namespace cpu {

static bool getCpuHal() {
    gCpuHal = ICpu::getService(); // Proxy to the service
    return gCpuHal != nullptr;
}

static void nativeInit(JNIEnv* env, jobject) {
    gCpuHalMutex.lock();
    getCpuHal();
    gCpuHalMutex.unlock();
}

static jboolean setScalingGovernor(JNIEnv* env, jclass, jstring governor) {
    const char* cGovernor = env->GetStringUTFChars(governor, NULL);
    ret = gCpuHal->setScalingGovernor(cGovernor);
    env->ReleaseStringUTFChars(governor, cGovernor);
    return ret;
}

static const char *classPathName = "com/windfan/Cpu";

static JNINativeMethod methods[] = {
    { "nativeInit", "()V", (void*) nativeInit },
    { "nativeSetScalingGovernor", "(Ljava/lang/String;)Z", (void*) setScalingGovernor },
};

// Register several native methods for one class.
static int registerNativeMethods(JNIEnv* env, const char* className,
    JNINativeMethod* gMethods, int numMethods) {
    myClass = env->FindClass(className);
    env->RegisterNatives(myClass, gMethods, numMethods);
    return JNI_TRUE;
}

int register_windfan_Cpu(JNIEnv* env) {
    if (!registerNativeMethods(env, classPathName, methods,
            sizeof(methods) / sizeof(methods[0]))) {
        return JNI_FALSE;
    }
    return JNI_TRUE;
}

} /* namespace cpu */
