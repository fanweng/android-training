void main() {
    service = new Cpu();
    service->registerAsService();

    configureRpcThreadPool();
    joinRpcThreadpool();
}
