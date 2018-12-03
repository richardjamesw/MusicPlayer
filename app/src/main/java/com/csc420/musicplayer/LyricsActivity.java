package com.csc420.musicplayer;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LyricsActivity extends Activity {

    Button btnExit;
    TextView lyricsText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lyrics_activity);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * .9), (int)(height * .9));

        btnExit = findViewById(R.id.btnExit);
        lyricsText = findViewById(R.id.lyricsText);

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        lyricsText.setMovementMethod(new ScrollingMovementMethod());
        lyricsText.setText("Astro, yeah\n" +
                "\n" +
                "Sun is down, freezin' cold\n" +
                "That's how we already know winter's here\n" +
                "My dawg would prolly do it for a Louis belt\n" +
                "That's just all he know, he don't know nothin' else\n" +
                "I tried to show 'em, yeah\n" +
                "I tried to show 'em, yeah, yeah\n" +
                "Yeah, yeah, yeah\n" +
                "Gone on you with the pick and roll\n" +
                "Young LaFlame, he in sicko mode\n" +
                "\n" +
                "Woo, made this here with all the ice on in the booth\n" +
                "At the gate outside, when they pull up, they get me loose\n" +
                "Yeah, Jump Out boys, that's Nike boys, hoppin' out coupes\n" +
                "This shit way too big, when we pull up give me the loot\n" +
                "(Gimme the loot!)\n" +
                "Was off the Remy, had a Papoose\n" +
                "Had to hit my old town to duck the news\n" +
                "Two-four hour lockdown, we made no moves\n" +
                "Now it's 4AM and I'm back up poppin' with the crew\n" +
                "I just landed in, Chase B mixes pop like Jamba Juice\n" +
                "Different colored chains, think my jeweler really sellin' fruits\n" +
                "And they chokin', man, know the crackers wish it was a noose\n" +
                "\n" +
                "Some-some-some, someone said\n" +
                "To win the retreat, we all in too deep\n" +
                "P-p-playin' for keeps, don't play us for weak (someone said)\n" +
                "To win the retreat, we all in too deep\n" +
                "P-p-playin' for keeps, don't play us for weak (yeah)\n" +
                "\n" +
                "This shit way too formal, y'all know I don't follow suit\n" +
                "Stacey Dash, most of these girls ain't got a clue\n" +
                "All of these hoes I made off records I produced\n" +
                "I might take all my exes and put 'em all in a group\n" +
                "Hit my esés, I need the bootch\n" +
                "'Bout to turn this function to Bonnaroo\n" +
                "Told her \"hop in, you comin' too\"\n" +
                "In the 305, bitches treat me like I'm Uncle Luke\n" +
                "(Don't stop, pop that pussy!)\n" +
                "Had to slop the top off, it's just a roof (uh)\n" +
                "She said \"where we goin'?\" I said \"the moon\"\n" +
                "We ain't even make it to the room\n" +
                "She thought it was the ocean, it's just the pool\n" +
                "Now I got her open, it's just the Goose\n" +
                "Who put this shit together? I'm the glue (someone said)\n" +
                "Shorty FaceTimed me out the blue\n" +
                "\n" +
                "Someone said (Playin' for keeps)\n" +
                "Someone said, motherfuck what someone said\n" +
                "(Don't play us for weak)\n" +
                "\n" +
                "Yeah\n" +
                "Astro\n" +
                "Yeah, yeah\n" +
                "Tay Keith, fuck these n---as up (Ay, ay)\n" +
                "\n" +
                "She's in love with who I am\n" +
                "Back in high school, I used to bus it to the dance (yeah)\n" +
                "Now I hit the FBO with duffles in my hands\n" +
                "I did half a Xan, thirteen hours 'til I land\n" +
                "Had me out like a light, ayy, yeah\n" +
                "Like a light, ayy, yeah\n" +
                "Like a light, ayy\n" +
                "\n" +
                "Slept through the flight, ayy\n" +
                "Knocked for the night, ayy, 767, man\n" +
                "This shit got double bedroom, man\n" +
                "I still got scores to settle, man\n" +
                "I crept down the block (down the block), made a right (yeah, right)\n" +
                "Cut the lights (yeah, what?), paid the price (yeah)\n" +
                "N---as think it's sweet (nah, nah), it's on sight (yeah, what?)\n" +
                "Nothin' nice (yeah), baguettes in my ice (aww, man)\n" +
                "Jesus Christ (yeah), checks over stripes (yeah)\n" +
                "That's what I like (yeah), that's what we like (yeah)\n" +
                "Lost my respect, you not a threat\n" +
                "When I shoot my shot, that shit wetty like I'm Sheck (bitch!)\n" +
                "See the shots that I took (ayy), wet like I'm Book (ayy)\n" +
                "Wet like I'm Lizzie, I be spinnin' Valley\n" +
                "Circle blocks 'til I'm dizzy (yeah, what?)\n" +
                "Like where is he? (Yeah, what?)\n" +
                "No one seen him (yeah, yeah)\n" +
                "I'm tryna clean 'em (yeah)\n" +
                "\n" +
                "She's in love with who I am\n" +
                "Back in high school, I used to bus it to the dance\n" +
                "Now I hit the FBO with duffles in my hand (woo!)\n" +
                "I did half a Xan, thirteen hours 'til I land\n" +
                "Had me out like a light, like a light\n" +
                "Like a light, like a light\n" +
                "Like a light (yeah), like a light\n" +
                "Like a light\n" +
                "\n" +
                "Yeah, passed the dawgs a celly\n" +
                "Sendin' texts, ain't sendin' kites, yeah\n" +
                "He said \"keep that on lock\"\n" +
                "I said \"you know this shit, it’s life”, yeah\n" +
                "It's absolute (yeah), I'm back reboot (it's lit!)\n" +
                "LaFerrari to Jamba Juice, yeah (skrrt, skrrt)\n" +
                "We back on the road, they jumpin' off, no parachute, yeah\n" +
                "Shawty in the back\n" +
                "She said she workin' on her glutes, yeah (oh my God)\n" +
                "Ain't by the book, yeah\n" +
                "This how it look, yeah\n" +
                "'Bout a check, yeah\n" +
                "Just check the foots, yeah\n" +
                "Pass this to my daughter, I'ma show her what it took (yeah)\n" +
                "Baby mama cover Forbes, got these other bitches shook, yeah\n" +
                "\n" +
                "Ye-ah");

    }
}
