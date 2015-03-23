## BitSharesHash

This is an alternative GUI (Graphical User Interface) for BitShares. 

Our main purpose was to create a fully functional, user-friendly and aesthetically pleasing BitShares wallet, eventually suitable for mass adoption. We would like to offer it to the community, not as a competing product but rather as a complementary solution meant to enhance the adoption of BitShares.

At this stage it is not a completely finished product, but rather our take on wallet architecture in general, offering what we would hope are unique features.

### Technical background
Whereas the official wallet is built in JavaScript ours is built in Java. In technical terms it is a JavaFX desktop application talking to the BitShares API using JSON encoding. It connects to the official BitShares client running in the background. This way our code is only responsible for the visual layer and all heavy-weight wallet processing is secure as it is performed in the background by the official BitShares client.
 
### Main features

* An extended two-stage menu clearly divides the app into five distinct modules: Payments, Community, Exchange, System and Settings.
* The main menu offers a detailed description of each section of every module, thus simplifying the learning process for first time users.
* Main pages are accessible through the menu whereas pages displaying an item's details (e.g. account details or markets) are managed separately and can be minimized to the bottom bar and brought back to view with a single click. It works just like a task bar in a standard operating system.
* We handle multitasking by allowing the user to have several pages open at the same time and easily switch between them. This includes the market pages and allows you to trade on two markets simultaneously.
* The GUI features an overview showing a cross-section summary of all important information from all the modules.
* The snapshot view gives you a list of all recently opened pages. This acts as an alternative way to navigate through the app.
* Balances are displayed in two ways: by accounts and by assets. This way you can see how much money you have in total.
* All user actions are guided through comprehensive wizards ending with a clear confirmation of what is about to happen.
* The market pages offer big and scalable charts, showing both price history and market depth.
* We've introduced the concept of delegate announcements and market news. As this feature is not supported by the blockchain, an external web service will be required to supply the data.
* The app can handle internet links and display web content internally. This means that any announcement a delegate needs to make public can be easily integrated into the wallet.
* Our layout is meant to be very mobile-friendly, ideally targeted for small laptops and tablets. Despite all the complexity everything fits into one screen without resorting to multiple pop-up windows. The layout is fully scalable and can fit both small and large screens.
* The default colors can be changed by the user. It's all CSS-based so the app can easily have multiple color schemes.
* All lists (e.g. accounts, assets, markets) have a search utility for easy browsing. The transaction list have additional search criteria.
* There is a fully implemented back and forward navigation, just like in a web browser. Also, all relevant data inside the app is clickable, just like internet hyperlinks.
* The transfer wizard offers multiple options, including a transfer to a public key and a transfer to an ad-hoc account name.
* Drag-and-drop feature offers an easy way to make money transfers quickly.
* The whole architecture is very modular with the modules being loosely coupled, meaning it is possible to take a small part of it (e.g. the market page) and launch it as a separate application and then develop it further (e.g. into a trading platform).

### Features not implemented yet
* Real data for delegate announcements and market news.
* Introduction wizard and wallet creation for first-time users.
* Preferences and wallet management features.
* Language localization.
Obviously, whenever there are dummy lorem ipsum texts - these need to be replaced.
As of now, this app has not gone through the hands of a professional graphic designer and we realize that some graphical features will have to be improved.
Some of the icons and images have been grabbed from the internet, and for aesthetic and legal reasons will have to be replaced by custom made ones.

### [Screenshots] (http://neura.sx/bts/screens)

### [Try it out using virtual machine](https://github.com/neura-sx/BitSharesHash/blob/master/README.VirtualMachine.md)

### [Compile the code in Eclipse](https://github.com/neura-sx/BitSharesHash/blob/master/README.CompileInEclipse.md)






