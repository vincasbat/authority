package authortyfile;

import generated.ApplicationReference;
import generated.AuthorityFile;
import generated.AuthorityFileDoc;
import generated.DocumentId;
import generated.ObjectFactory;
import generated.PriorityClaim;
import generated.PriorityClaims;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 *
 * @author vincasbat ( vincasbat@gmail.com )
 */
public class AuthorityEP {

  public static List < String > getEPlist() {
    String pubdata = JOptionPane.showInputDialog("dtbupli < : (pvz., 2009-10-10)");

    List < String > list = new ArrayList < > ();
    //if (true) return list;         

    try {
      Class.forName("com.informix.jdbc.IfxDriver");
      String URL = "jdbc:informix-sqli://0.0.0.0:2000/europat:INFORMIXSERVER=ol_cs_dbs;user=informix;password=000";
      Connection conn = DriverManager.getConnection(URL);

      //Patentai:
      String ep_patentai = "SELECT extidpatent, lgstappli from ptappli, gazette, publication " +
        "where ptappli.idappli=publication.idappli and (nosect=2 or nosect=52) " +
        "and publication.nogazette=gazette.nogazette and publication.yygazette=gazette.yygazette " +
        " and stgazette=2  and lgstappli in (20, 30, 85, 95, 86, 96, 97, 92)" +
        " and dtpubli <  '" + pubdata + "'" +
        "order by extidpatent, dtpubli, dtappli ";

      System.out.println(ep_patentai);
      Statement stmtp = conn.createStatement();
      ResultSet rspubp = stmtp.executeQuery(ep_patentai);
      int j = 0;
      if (rspubp != null) {
        while (rspubp.next()) {
          list.add(rspubp.getString("extidpatent").trim()); //?trim
          System.out.println(rspubp.getString("extidpatent"));
          j++;
        } //while
      }
      System.out.println("patentu: " + j);
      rspubp.close();
      stmtp.close();
      conn.close();
    } //getLTlist
    catch (ClassNotFoundException ex) {
      Logger.getLogger(AuthortyFile.class.getName()).log(Level.SEVERE, null, ex);
    } catch (SQLException ex) {
      Logger.getLogger(AuthortyFile.class.getName()).log(Level.SEVERE, null, ex);
    }

    return list;
  }

  public static void sudarytiEP(List < String > ep_patentai, String fn) {

    try {

      Class.forName("com.informix.jdbc.IfxDriver");

      String URL = "jdbc:informix-sqli://192.168.10.9:2000/europat:INFORMIXSERVER=ol_cs_dbs;user=informix;password=info123";

      Connection conn = DriverManager.getConnection(URL);

      Calendar cal = Calendar.getInstance();
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
      String dateProduced = sdf.format(cal.getTime());

      ObjectFactory of = new ObjectFactory();
      AuthorityFile af = of.createAuthorityFile();
      int lg_status = 0;
      af.setCountry("LT");
      af.setDateProduced(dateProduced);
      String idappli = null;
      String extidappli = null;
      String dtapp = null;
      String GET_PAT = "select idappli, extidappli, bedat(dtappli) dtapp, lgstappli  from informix.ptappli  where extidpatent = ";
      Statement stmt = conn.createStatement();
      int j = 0;
      if (ep_patentai != null) {
        while (j < ep_patentai.size()) {

          //    if (ep_patentai.get(j).toString().trim().length()==4){// tai yra patentas

          String GET_PATID = GET_PAT + "\"" + ep_patentai.get(j).toString() + "\"";
          ResultSet rs = stmt.executeQuery(GET_PATID);

          while (rs.next()) {
            idappli = rs.getString("idappli");
            lg_status = rs.getInt("lgstappli");
            extidappli = rs.getString("extidappli").trim();
            dtapp = rs.getString("dtapp");
            //   System.out.println(ep_patentai.get(j)+" "+extidappli +" "+dtapp);  
            AuthorityFileDoc afd = of.createAuthorityFileDoc();
            DocumentId di = of.createDocumentId();
            di.setCountry("EP");
            di.setDocNumber(ep_patentai.get(j).substring(2)); //??????????????????????
            di.setKind("B");

            String dtpub = null;
            String GET_DTPUBLI = "select bedat(gazette.dtpubli) dtpb from informix.gazette, informix.publication where " + "idappli = " + "\"" + idappli.trim() + "\"" +
              " and gazette.nogazette = publication.nogazette and gazette.yygazette = publication.yygazette and (nosect=2 or nosect=52)";
            Statement stmtrspub = conn.createStatement();
            ResultSet rspub = stmtrspub.executeQuery(GET_DTPUBLI);
            while (rspub.next()) {
              dtpub = rspub.getString("dtpb");
            }
            rspub.close();
            stmtrspub.close();
            //      System.out.println(GET_DTPUBLI);
            System.out.println(ep_patentai.get(j).toString() + "dtpub: " + dtpub);
            if (dtpub == null) {
              di.setDate("unknown");
            } else {
              di.setDate(dtpub);
            }

            afd.setDocumentId(di);

            afd.setPubInd("");
            ApplicationReference ar = of.createApplicationReference();
            ar.setCountry("EP");
            ar.setDate(dtapp);
            ar.setDocNumber(extidappli.substring(2)); //????????????????
            ar.setKind("A");

            afd.setApplicationReference(ar);

            PriorityClaims priorityClaims = of.createPriorityClaims();

            String GET_PRIO = "select idappli, odprio, typrio, idcountry, noprio, bedat(dtprio) prio_data from priority where idappli = " + "\"" + idappli + "\"";
            Statement stmtprio = conn.createStatement();
            ResultSet rsprio = stmtprio.executeQuery(GET_PRIO);
            int typrio = 0;
            int odprio = 0;
            int prios = 0;
            String idcountry_prio = null;
            String noprio = null;
            String prio_data = null;

            while (rsprio.next()) {
              typrio = rsprio.getInt("typrio");
              odprio = rsprio.getInt("odprio");
              idcountry_prio = rsprio.getString("idcountry");
              noprio = rsprio.getString("noprio").trim();
              prio_data = rsprio.getString("prio_data");
              PriorityClaim priorityClaim = of.createPriorityClaim();
              priorityClaim.setSequence(Integer.toString(odprio));

              /*
                            Koks prioritetas (national, regional ar international) turi būti nustatoma
pagal valstybės kodą;
International – valstybės kodai WO, IB
Regional – AP,EA, EP, OA,XN, XV
National – visi kiti kodai.

                            */
              switch (idcountry_prio) {
              case "WO":
              case "IB":
                priorityClaim.setKind("international");
                break;
              case "AP":
              case "EA":
              case "EP":
              case "OA":
              case "XN":
              case "XV":
                priorityClaim.setKind("regional");
                break;

              default:
                priorityClaim.setKind("national");
              }

              priorityClaim.setDate(prio_data);
              priorityClaim.setDocNumber(noprio);
              priorityClaim.setCountry(idcountry_prio);
              priorityClaims.getPriorityClaim().add(priorityClaim);
              prios++;
            } // while prio
            rsprio.close();
            stmtprio.close();
            if (prios > 0) {

              afd.setPriorityClaims(priorityClaims);
            } //if prios>0  

            af.getAuthorityFileDoc().add(afd);
          } //while rs.next

          j++;
        } //while size
      } //if nut null

      conn.close();

      JAXBContext jaxbContext = JAXBContext.newInstance(AuthorityFile.class);

      Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
      jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

      jaxbMarshaller.setProperty("com.sun.xml.internal.bind.xmlHeaders", "\n<!DOCTYPE authority-file SYSTEM \"authority-file.dtd\">");

      OutputStream os = new FileOutputStream(fn);
      os.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>".getBytes(Charset.forName("UTF-8")));
      jaxbMarshaller.marshal(af, os); //new FileOutputStream("spc.xml")
      jaxbMarshaller.marshal(af, System.out);

      os.close();

    } catch (JAXBException e) {

      e.printStackTrace();
    } catch (ClassNotFoundException ex) {
      Logger.getLogger(AuthortyFile.class.getName()).log(Level.SEVERE, null, ex);
    } catch (SQLException ex) {
      Logger.getLogger(AuthortyFile.class.getName()).log(Level.SEVERE, null, ex);
    } catch (FileNotFoundException ex) {
      Logger.getLogger(AuthortyFile.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      Logger.getLogger(AuthortyFile.class.getName()).log(Level.SEVERE, null, ex);
    }
    JOptionPane.showMessageDialog(null, "Darbas baigtas", "Valstybinis patentų biuras", JOptionPane.INFORMATION_MESSAGE);
  }

}
