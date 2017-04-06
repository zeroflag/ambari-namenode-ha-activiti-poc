package com.example.ui;

import java.io.Serializable;
import java.util.List;

public class Hosts implements Serializable {
  public final String currentNameNodeHost;
  public final String newNameNodeHost;
  public final List<String> journalNodeHosts;

  public Hosts(String currentNameNodeHost, String newNameNodeHost, List<String> journalNodeHosts) {
    this.currentNameNodeHost = currentNameNodeHost;
    this.newNameNodeHost = newNameNodeHost;
    this.journalNodeHosts = journalNodeHosts;
  }
}
