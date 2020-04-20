package com.jemmaengz.pvj;

import com.jemmaengz.pvj.model.HibernateUtil;

public class Start {
  public static void main(String[] args) {
    Main.session = HibernateUtil.getSessionFactory().openSession();
    Main.main(args);
  }
}