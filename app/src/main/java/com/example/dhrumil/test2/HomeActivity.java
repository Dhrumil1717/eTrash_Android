package com.example.dhrumil.test2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dhrumil.test2.Model.Products;
import com.example.dhrumil.test2.ViewHolder.ProductViewHolder;
import com.facebook.login.LoginManager;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

import static java.lang.String.valueOf;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    Button blogout, bAnonymousSignup;
    FirebaseAuth mAuth;
    FirebaseUser user;
    TextView tvname, tvemail;
    ImageView ivphoto;
    NavigationView navigationView;
    DrawerLayout drawer;
    Toolbar toolbar;
    DatabaseReference ProductRef;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    String productId = "";
    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Paper.init(this);
        ProductRef = FirebaseDatabase.getInstance().getReference().child("Products");//Same name as specified in firebase database

        productId = getIntent().getStringExtra("pid");
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        blogout = findViewById(R.id.blogout);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        tvemail = headerView.findViewById(R.id.tvemail);
        tvname = headerView.findViewById(R.id.name);
        ivphoto = headerView.findViewById(R.id.imageView);
        drawer = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        bAnonymousSignup = headerView.findViewById(R.id.banonymous);
        fab = findViewById(R.id.fab);

        bAnonymousSignup.setVisibility(View.GONE);

        bAnonymousSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CustomerLogin.class));
            }
        });


        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CartActivity.class));

            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        blogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    mAuth.signOut();
                    LoginManager.getInstance().logOut();
                    finish();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                } else {
                    Toast.makeText(HomeActivity.this, "" + user, Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if (user != null) {

            if (user.isAnonymous()) {

                bAnonymousSignup.setVisibility(View.VISIBLE);
            } else {
                tvname.setText(user.getDisplayName());
                tvemail.setText(user.getEmail());
                Picasso.get().load(valueOf(user.getPhotoUrl())).into(ivphoto);
            }

        }
        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>().setQuery(ProductRef, Products.class).build();
        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @SuppressLint("RestrictedApi")
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {
                holder.txtproductname.setText(model.getName());
                holder.txtproductdescription.setText(model.getDescription());
                holder.txtproductprice.setText("Price = " + model.getPrice() + "RS");
                Picasso.get().load(model.getImage()).into(holder.imageView);

                if (user.isAnonymous()) {
                    fab.setVisibility(View.GONE);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(HomeActivity.this, "You can add products to your cart once you login", Toast.LENGTH_SHORT).show();

                        }
                    });
                } else {
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(HomeActivity.this, ProductDetailsActivity.class);
                            intent.putExtra("pid", model.getPid());
                            intent.putExtra("CompanyName", model.getCompanyname());
                            startActivity(intent);
                        }
                    });
                }

            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_item_layout, viewGroup, false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;


            }
        };


        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_cart) {
            startActivity(new Intent(getApplicationContext(), CartActivity.class));
        } else if (id == R.id.nav_logout) {
            Paper.book().destroy();
            startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));

            if (user != null) {
                mAuth.signOut();
                LoginManager.getInstance().logOut();
                finish();
                startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));

            } else {
                Toast.makeText(HomeActivity.this, "" + user, Toast.LENGTH_SHORT).show();
            }
            finish();

        } else if (id == R.id.nav_sell) {
            startActivity(new Intent(HomeActivity.this, Sell.class));
        } else if (id == R.id.nav_ourProcess) {
            startActivity(new Intent(HomeActivity.this, OurProcess.class));
        } else if (id == R.id.nav_ContactUs) {
            startActivity(new Intent(HomeActivity.this, ContactUs.class));
        } else if (id == R.id.nav_AboutUs) {
            startActivity(new Intent(HomeActivity.this, AboutUs.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
