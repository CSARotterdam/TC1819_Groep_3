package nl.group3.techlab;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class contact extends AppCompatActivity {

//    TextView cTitle;
//    TextView CP1,CP2,CP3,CP4,CP5;
    TextView visitingh,times,location,adress,contactinfo,infocontact, namecontact;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("Techlab", 0);
        int d_color = sharedPreferences.getInt("d_color", 1);
        switch (d_color) {
            case 1:
                setTheme(R.style.theme1);
                break;
            case 2:
                setTheme(R.style.theme2);
                break;
            default:
                break;
        }

        setContentView(R.layout.activity_contact);

/*        cTitle = (TextView) findViewById(R.id.contactTitle);
        CP1 = (TextView) findViewById(R.id.CPerson1);
        CP2 = (TextView) findViewById(R.id.CPerson2);
        CP3 = (TextView) findViewById(R.id.CPerson3);
        CP4 = (TextView) findViewById(R.id.CPerson4);
        CP5 = (TextView) findViewById(R.id.CPerson5);

        cTitle.setText(R.string.contact_titel);

        CP1.setText("Ricardo Stam – CSAR Voorzitter \n r.stam@csarotterdam.nl");
        CP2.setText("Dion van der Leer – CSAR Softwareontwikkeling \n d.vanderLeer@csarotterdam.nl");
        CP3.setText("Lou-Anne Koster – Peercoach \n 0950196@hr.nl");
        CP4.setText("Oguzcan Karakoç – Peercoach \n 0946448@hr.nl");
        CP5.setText("Okan Emeni – Peercoach \n 0950438@hr.nl");   */

        visitingh = (TextView) findViewById(R.id.openingstijden);
        times = (TextView) findViewById(R.id.tijden);
        location = (TextView) findViewById(R.id.locatie);
        adress = (TextView) findViewById(R.id.adres);
        contactinfo = (TextView) findViewById(R.id.contactgegevens);
        infocontact= (TextView) findViewById(R.id.infocontact);
        namecontact = (TextView) findViewById(R.id.naamcontact);

        visitingh.setText(R.string.openingstijden);
        times.setText(R.string.tijden);
        location.setText(R.string.locatie);
        adress.setText(R.string.adres);
        contactinfo.setText(R.string.contactgegevens);
        namecontact.setText("Maurice van Veen");
        infocontact.setText(Html.fromHtml ("<a href=\"mailto:0921718@hr.nl\">✉ 0921718@hr.nl</a>"));
        infocontact.setMovementMethod(LinkMovementMethod.getInstance());

    }
}
