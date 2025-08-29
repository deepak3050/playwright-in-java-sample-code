package com.serenitydojo.playwright;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;

public class SampleAddCart {
    protected static Playwright playwright;
    protected static Browser browser;
    protected static BrowserContext browserContext;

    Page page;

    @BeforeAll
    static void setUpBrowser() {
        playwright = Playwright.create();
        playwright.selectors().setTestIdAttribute("data-test");
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false)
                        .setArgs(Arrays.asList("--no-sandbox", "--disable-extensions", "--disable-gpu"))
        );
    }

    @BeforeEach
    void setUp() {
        browserContext = browser.newContext();
        page = browserContext.newPage();
    }

    @AfterEach
    void closeContext() {
        browserContext.close();
    }

    @AfterAll
    static void tearDown() {
        browser.close();
        playwright.close();
    }

    @BeforeEach
    void openHomePage() {
        page.navigate("https://practicesoftwaretesting.com");
    }

    @Test
    void withPageObjects() {
        SearchComponent searchComponent = new SearchComponent(page);
        ProductList productList = new ProductList(page);
        ProductDetails productDetails = new ProductDetails(page);
        NavBar navBar = new NavBar(page);

        searchComponent.searchBy("Pliers");
        productList.viewProductDetails("Combination Pliers");

        productDetails.increaseQuantityBye(2);
        productDetails.addToCart();
        navBar.openCart();

    }


    class SearchComponent {
        private final Page page;

        SearchComponent(Page page) {
            this.page = page;
        }

        public void searchBy(String keyword) {
            page.waitForResponse("**/products/search?q=" + keyword, () -> {
                page.getByPlaceholder("Search").fill(keyword);
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();
            });
        }
    }

    class ProductList {
        private final Page page;

        ProductList(Page page) {
            this.page = page;
        }


        public List<String> getProductNames() {
            return page.getByTestId("product-name").allInnerTexts();
        }

        public void viewProductDetails(String productName) {
            page.locator(".card").getByText(productName).click();
        }
    }

    class ProductDetails{
        private final Page page;
        ProductDetails(Page page){
            this.page = page;
        }
        public void increaseQuantityBye(int increment) {
            for (int i = 0; i < increment; i++) {
                page.getByTestId("increase-quantity").click();
            }
        }
        public void addToCart(){
            page.getByText("Add to cart");
            page.waitForResponse(response ->response.url().contains("/carts")&& response.request().method().equals("POST")
                    ,() -> page.getByText("Add to cart").click());
        }

    }

    class NavBar{
        private final Page page;
        NavBar(Page page){
            this.page = page;
        }
        public void openCart(){
            page.getByTestId("lblCartCount").click();

        }
    }


}

