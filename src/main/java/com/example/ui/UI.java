package com.example.ui;

public interface UI {
  String gettingStarted();
  Hosts selectHosts();
  String review();
  boolean manualStep1();
  boolean manualStep2();
  String manualStep3();
  void close();
}
