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
 * @author vincasbat
 */
public class AuthortyFile {

  public static List < String > getLTlist() {
    List < String > list = new ArrayList < > ();
    try {
      Class.forName("com.informix.jdbc.IfxDriver");
      String URL = "jdbc:informix-sqli://192.168.10.9:2000/patentai:INFORMIXSERVER=ol_cs_dbs;user=informix;password=info123";
      Connection conn = DriverManager.getConnection(URL);

      String lt_paraiskos = "SELECT   extidappli from ptappli, gazette, publication " +
        "where ptappli.idappli=publication.idappli and (nosect=1 or nosect=10) " +
        "and publication.nogazette=gazette.nogazette and publication.yygazette=gazette.yygazette " +
        "  and stgazette=2 and lgstappli in (15, 18, 20, 95, 96, 110, 111, 92, 98)" +
        "order by extidappli, dtpubli, dtappli";

      System.out.println(lt_paraiskos);
      Statement stmt = conn.createStatement();
      ResultSet rspub = stmt.executeQuery(lt_paraiskos);
      int j = 0;

      if (rspub != null) {
        while (rspub.next()) {
          list.add(rspub.getString("extidappli").trim());
          System.out.println(rspub.getString("extidappli")); //?trim
          j++;
        } //while
      }
      System.out.println("paraisku: " + j);
      rspub.close();
      stmt.close();

      String lt_patentai = "SELECT extidpatent from ptappli, gazette, publication " +
        "where ptappli.idappli=publication.idappli and nosect=2 " +
        "and publication.nogazette=gazette.nogazette and publication.yygazette=gazette.yygazette " +
        " and stgazette=2 and lgstappli in (11, 20, 95, 96, 110, 111, 92, 98)" +
        "order by extidpatent, dtpubli, dtappli ";

      System.out.println(lt_patentai);
      Statement stmtp = conn.createStatement();
      ResultSet rspubp = stmtp.executeQuery(lt_patentai);
      j = 0;
      if (rspub != null) {
        while (rspubp.next()) {
          list.add(rspubp.getString("extidpatent").trim());
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

  public static void sudarytiLT(List < String > lt_patentai_ir_paraiskos, String fn) {

    try {

      Class.forName("com.informix.jdbc.IfxDriver");

      String URL = "jdbc:informix-sqli://000.000.000.000:2000/patentai:INFORMIXSERVER=ol_cs_dbs;user=informix;password=000";

      Connection conn = DriverManager.getConnection(URL);

      Calendar cal = Calendar.getInstance();
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
      String dateProduced = sdf.format(cal.getTime());

      ObjectFactory of = new ObjectFactory();
      AuthorityFile af = of.createAuthorityFile();
      af.setCountry("LT");
      af.setDateProduced(dateProduced);
      String idappli = null;
      String extidappli = null;
      String dtapp = null;
      String extidpatent = null;
      String GET_PAT = "select idappli, extidappli, extidpatent, lgstappli, bedat(dtappli) dtapp  from informix.ptappli  where extidpatent = ";
      Statement stmt = conn.createStatement();
      int j = 0;
      if (lt_patentai_ir_paraiskos != null) {
        while (j < lt_patentai_ir_paraiskos.size()) {

          if (lt_patentai_ir_paraiskos.get(j).toString().trim().length() == 4) { // tai yra patentas

            String GET_PATID = GET_PAT + "\"" + lt_patentai_ir_paraiskos.get(j).toString() + "\"";
            ResultSet rs = stmt.executeQuery(GET_PATID);

            while (rs.next()) {
              idappli = rs.getString("idappli");
              extidappli = rs.getString("extidappli").trim();
              extidpatent = rs.getString("extidpatent");
              dtapp = rs.getString("dtapp");
              //   System.out.println(lt_patentai_ir_paraiskos.get(j)+" "+extidappli +" "+dtapp);  
              AuthorityFileDoc afd = of.createAuthorityFileDoc();
              DocumentId di = of.createDocumentId();
              di.setCountry("LT");
              di.setDocNumber(lt_patentai_ir_paraiskos.get(j));
              di.setKind("B");

              String dtpub = null;
              String GET_DTPUBLI = "select bedat(gazette.dtpubli) dtpb from informix.gazette, informix.publication where " + "idappli = " + "\"" + idappli + "\"" +
                " and gazette.nogazette = publication.nogazette and gazette.yygazette = publication.yygazette and nosect=2";
              Statement stmtrspub = conn.createStatement();
              ResultSet rspub = stmtrspub.executeQuery(GET_DTPUBLI);
              while (rspub.next()) {
                dtpub = rspub.getString("dtpb");
              }
              rspub.close();
              stmtrspub.close();

              di.setDate(dtpub);
              afd.setDocumentId(di);

              if (Integer.parseInt(extidpatent.trim()) < 3000) afd.setPubInd("B");
              else afd.setPubInd("");
              ApplicationReference ar = of.createApplicationReference();
              ar.setCountry("LT");
              ar.setDate(dtapp);
              ar.setDocNumber(extidappli.replace(" ", ""));
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

          } else { // tai yra paraiska
            //**************************************************** 
            int lg_status = 0;

            String GET_PAR = "select idappli, dtappli, stitle, engtitle, ipcmclass, extidappli, bedat(dtappli) dtapp, lgstappli from informix.ptappli  where extidappli = ";
            String GET_PARID = GET_PAR + "\"" + lt_patentai_ir_paraiskos.get(j).toString() + "\"";
            Statement stmt2 = conn.createStatement();
            ResultSet rs2 = stmt2.executeQuery(GET_PARID);

            while (rs2.next()) {
              idappli = rs2.getString("idappli");
              extidappli = rs2.getString("extidappli").trim();
              lg_status = rs2.getInt("lgstappli");
              dtapp = rs2.getString("dtapp");
              AuthorityFileDoc afd = of.createAuthorityFileDoc();
              DocumentId di = of.createDocumentId();
              di.setCountry("LT");
              di.setDocNumber(lt_patentai_ir_paraiskos.get(j).replace(" ", ""));
              di.setKind("A");

              String dtpub = null;

              String GET_DTPUBLI = "select bedat(gazette.dtpubli) dtpb from informix.gazette, informix.publication where " + "idappli = " + "\"" + idappli + "\"" +
                " and gazette.nogazette = publication.nogazette and gazette.yygazette = publication.yygazette and (nosect=1 or nosect=10)";
              Statement stmtrspub = conn.createStatement();
              ResultSet rspub = stmtrspub.executeQuery(GET_DTPUBLI);
              while (rspub.next()) {
                dtpub = rspub.getString("dtpb");
              }
              rspub.close();
              stmtrspub.close();
              //  System.out.println("dtpub paraiskos "+dtpub);  
              if (dtpub == null) {
                di.setDate("unknown");
              } else {
                di.setDate(dtpub);
              }
              afd.setDocumentId(di);

              if (lg_status == 18) afd.setPubInd("W");
              else afd.setPubInd("");
              ApplicationReference ar = of.createApplicationReference();
              ar.setCountry("LT");
              ar.setDate(dtapp);
              ar.setDocNumber(extidappli.replace(" ", ""));
              ar.setKind("A");

              afd.setApplicationReference(ar);
              af.getAuthorityFileDoc().add(afd);

              //Prioritetai  
              PriorityClaims priorityClaims = of.createPriorityClaims();
              String GET_PRIO = "select noprio, idcountry, bedat(dtprio) dtpri, odprio, typrio from priority where idappli = " + "\"" + idappli + "\"";
              ResultSet rsprio = stmt.executeQuery(GET_PRIO);
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
                prio_data = rsprio.getString("dtpri");
                PriorityClaim priorityClaim = of.createPriorityClaim();
                priorityClaim.setSequence(Integer.toString(odprio));

                switch (typrio) {
                case 1:
                  priorityClaim.setKind("international");
                  break;
                case 3:
                  priorityClaim.setKind("national");
                  break;
                default:
                  priorityClaim.setKind("----------------------------------nežinomas prioritetas----------------------------");
                  break;
                }

                priorityClaim.setDate(prio_data);
                priorityClaim.setDocNumber(noprio);
                priorityClaim.setCountry(idcountry_prio);
                priorityClaims.getPriorityClaim().add(priorityClaim);
                prios++;
              } // while prio
              if (prios > 0) {
                afd.setPriorityClaims(priorityClaims);
              }
              rsprio.close();

            } //while rs2.next()
            rs2.close();
            stmt2.close();

            //****************************************                       
          } //else paraiska
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
      jaxbMarshaller.marshal(af, os);
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
