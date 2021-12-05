package com.irvan.aurashop.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.irvan.aurashop.models.*
import com.irvan.aurashop.models.Orders
import com.irvan.aurashop.ui.activities.*
import com.irvan.aurashop.ui.fragments.*
import com.irvan.aurashop.utils.Constant

class
FirestoreClass {

    private val mFirestore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User){
        //"users" adalah sebuah nama koleksi,jika koleksi tersebut sebelumnya sudah dibuat maka tidak akan membuat koleksi yang bernama sama karena akan menciptakan ambigu
        mFirestore.collection(Constant.USERS)
                //document atau dokumen ID untuk field user,ini adalah bentuk id untuk user yang saya tulis UID yang sama pada kelas ProfileModel
                .document(userInfo.id)
                .set(userInfo, SetOptions.merge())
                .addOnSuccessListener {
                    activity.userRegistrationSuccess()
                }
                .addOnFailureListener { e -> activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName,"Terjadi kesalahan selama registrasi user",e)}
    }

    fun getCurrentUserID(): String{
        //sebuah instance dari currentuser menggunakan FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        //sebuah variabel untuk assign currentUserID pada fungsi currentUserId jika tidak null atau tidak ada else maka akan menjadi kosong
        var currentUserID = ""
        if (currentUser != null){
            currentUserID = currentUser.uid
        }

        return currentUserID
    }

    fun getUserDetails(activity: Activity){

        //disini kita melewati nama koleksi dari mana data yang akan kita ambil
        mFirestore.collection(Constant.USERS)
        //dokumen id digunakan untuk mendapatkan field user
                .document(getCurrentUserID())
                .get()
                .addOnSuccessListener { document ->
                    Log.i(activity.javaClass.simpleName, document.toString())

                    //disini kita telah menerima dokumen snapshot yang mana telah dikonversi pada user data model object
                    val user = document.toObject(User::class.java)!!

                    // Membuat Instance dari editor yang akan membantu mengubah SharedPreference
                    val sharedPreferences =
                            activity.getSharedPreferences(Constant.APP_PREFERENCE_NAME,Context.MODE_PRIVATE)
                    val editor : SharedPreferences.Editor = sharedPreferences.edit()
                    //key : logged_in_username
                    //Value : user.firstname & user.lastName
                    editor.putString(Constant.LOGGED_IN_USERNAME, "${user.firstName} ${user.lastName}")
                    editor.apply()

                    //6 langkah harus dilewati untuk menuju login activity
                        //DIMULAI
                    when(activity){
                        is LoginActivity -> {
                            //memanggil fungsi dari base activity untuk mentransfer hasilnya pada itu
                            activity.userLoggedInSuccess(user)
                        }
                        is SettingsActivity -> {
                            //mendapatkan user dari val user = document.toObject(User::class.java)!!
                            activity.userDetailSuccess(user)
                        }
                    }
                    //SELESAI
                }
                .addOnFailureListener { e ->
                    //menyembunyikan progress dialog jika ada error dan mencetak error kedalam log
                    when(activity){
                        is LoginActivity -> {
                            activity.hideProgressDialog()
                        }
                        is SettingsActivity -> {
                            activity.hideProgressDialog()
                        }
                    }

                    Log.e(activity.javaClass.simpleName,
                    "Terjadi kesalahan selama mendapatkan data user",e)
                }
    }

    fun completeUserProfileData(activity: Activity, userHashMap: HashMap<String, Any>){
        mFirestore.collection(Constant.USERS)
                .document(getCurrentUserID())
                .update(userHashMap)
                .addOnSuccessListener {
                    when (activity) {
                        is CompleteProfileUserActivity -> {
                            //menyembunyikan progress dialog jika ada error dan mencetaknya ke dalam log
                            activity.completeProfileSuccess()
                        }
                    }
                }
                .addOnFailureListener { e ->
                    when (activity) {
                        is CompleteProfileUserActivity -> {
                            //menyembunyikan progress dialog jika ada error dan mencetaknya ke dalam log
                            activity.hideProgressDialog()
                        }
                    }

                    Log.e(
                            activity.javaClass.simpleName,
                        "Terjadi kesalahan selama memperbarui detail user",
                        e
                    )
                }
    }

    fun uploadImageToCloudStorage(activity: Activity, imageFileURI: Uri?, imageType:String){
        val storageReference: StorageReference = FirebaseStorage.getInstance().reference.child(
                imageType + System.currentTimeMillis() + "."
                + Constant.getFileExtension(activity,imageFileURI)
        )

        storageReference.putFile(imageFileURI!!).addOnSuccessListener { taskSnapshot ->
            Log.e("Firebase Photo URL",taskSnapshot.metadata!!.reference!!.downloadUrl.toString())

            //mendapatkan  download url dari task snapshot
            taskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { uri ->
                        Log.e("Downloadable Image URL", uri.toString())
                        when(activity){
                            is CompleteProfileUserActivity -> {
                                activity.imageUploadSuccess(uri.toString())
                            }
                            is AddProductActivity -> {
                                activity.imageUploadSuccess(uri.toString())
                            }
                        }


                    }
                    .addOnFailureListener { exception ->

                        //Menyembunyikan progress dialog jika ada error dan mencetaknya ke dalam log
                        when(activity){
                            is RegisterActivity -> {
                                activity.hideProgressDialog()
                            }
                            is AddProductActivity -> {
                                activity.hideProgressDialog()
                            }
                        }

                        Log.e(
                                activity.javaClass.simpleName,
                                exception.message,
                                exception
                        )
                    }
                    }

        }

    fun uploadProductDetails(activity: AddProductActivity, productInfo: Product){
        mFirestore.collection(Constant.PRODUCTS)
                .document()
                .set(productInfo, SetOptions.merge())
                .addOnCompleteListener {
                    activity.productUploadSuccessPopup()
                }
                .addOnFailureListener { e ->

                    activity.hideProgressDialog()

                    Log.e(
                            activity.javaClass.simpleName,
                            "Error while uploading the product details.",
                            e
                    )
                }
    }

    fun getProductsList(fragment:Fragment){
        mFirestore.collection(Constant.PRODUCTS)
                .whereEqualTo(Constant.USER_ID,getCurrentUserID())
                .get()
                .addOnSuccessListener { document ->
                    Log.e("Daftar Produk",document.documents.toString())
                    val productList: ArrayList<Product> = ArrayList()
                    for (i in document.documents){
                        val product = i.toObject(Product::class.java)
                        product!!.product_id = i.id

                        productList.add(product)
                    }

                    when(fragment){
                        is ProductsFragment -> {
                            fragment.successProductListFromFirestore(productList)
                        }
                    }
                }
    }

    fun getProductDetails(activity: ProductDetailsActivity, productId: String){
        mFirestore.collection(Constant.PRODUCTS)
                .document(productId)
                .get()
                .addOnSuccessListener { document ->
                    val product = document.toObject(Product::class.java)
                    if (product != null) {
                        activity.productDetailSuccess(product)
                    }
                }
                .addOnFailureListener {
                    e ->
                    //menyembunyikan progress dialog bila ada kesalahan
                    activity.hideProgressDialog()

                    Log.e(activity.javaClass.simpleName,"Terjadi kesalahan selama mendapatkan informasi detail produk",e)
                }
    }

    fun addWishlistItems(activity: ProductDetailsActivity, addToWishlist: WishlistItem){
        mFirestore.collection(Constant.WISHLIST_ITEMS)
            .document()
            .set(addToWishlist, SetOptions.merge())
            .addOnSuccessListener {
                activity.addToWishlistSuccess()
            }.addOnFailureListener {
                e ->
                activity.hideProgressDialog()

                Log.e(activity.javaClass.simpleName,"Terjadi kesalahan dalam menambahkan wishlist",e)
            }
    }

    fun addCartItems(activity: ProductDetailsActivity, addToCart: CartItem){
        mFirestore.collection(Constant.CART_ITEMS) //membuat collection baru pada database FireStore di web
                .document()
                .set(addToCart, SetOptions.merge())
                .addOnSuccessListener {
                 activity.addToCartSuccess()
                }.addOnFailureListener {
                    e ->
                    activity.hideProgressDialog()

                    Log.e(activity.javaClass.simpleName,"Terjadi kesalahan selama menambah produk ke keranjang",e)
                }
    }

    fun deleteProduct(fragment: ProductsFragment, productId: String){
        mFirestore.collection(Constant.PRODUCTS)
                .document(productId)
                .delete()
                .addOnSuccessListener{
                    fragment.productDeleteSuccess()
                }.addOnFailureListener {
                    e ->

                    fragment.hideProgressDialog()

                    Log.e(
                            fragment.requireActivity().javaClass.simpleName,
                            "Terjadi error selagi menghapus produk",
                            e

                    )
                }
    }


    fun updateWishlist(context: Context, cart_id: String, itemHashMap: HashMap<String, Any>){
        mFirestore.collection(Constant.WISHLIST_ITEMS)
                .document(cart_id)
                .update(itemHashMap)
                .addOnSuccessListener {

                    when(context){
                        is WishlistActivity -> {
                            context.itemUpdateSuccess()
                        }
                    }

                }.addOnFailureListener {
                    e->

                    when(context){
                        is WishlistActivity -> {
                            context.hideProgressDialog()
                        }
                    }

                    Log.e(
                            context.javaClass.simpleName,"Terjadi kesalahan selama mengupdate item wishlist",e
                    )
                }
    }

    fun getWishlistItem(activity: Activity){
        mFirestore.collection(Constant.WISHLIST_ITEMS)
                .whereEqualTo(Constant.USER_ID, getCurrentUserID())
                .get()
                .addOnSuccessListener { document ->
                    Log.e(activity.javaClass.simpleName, document.documents.toString())

                    val list:ArrayList<WishlistItem> = ArrayList()

                    for (i in document.documents){

                        val wishlistItem = i.toObject(WishlistItem::class.java)!!
                        wishlistItem.id = i.id

                        list.add(wishlistItem)
                    }

                    when(activity){
                        is WishlistActivity -> {
                            activity.SuccessWishlistItem(list)
                        }
                    }
                }.addOnFailureListener { e ->
                    when(activity){
                        is WishlistActivity -> {
                            activity.hideProgressDialog()
                        }
                    }
                    Log.e(activity.javaClass.simpleName,"Terjadi kesalahan selama mendapatkan data item produk keranjang",e)
                }
    }


    fun getCartList(activity: Activity){
        mFirestore.collection(Constant.CART_ITEMS)
                .whereEqualTo(Constant.USER_ID, getCurrentUserID())
                .get()
                .addOnSuccessListener { document ->

                    Log.e(activity.javaClass.simpleName, document.documents.toString())

                    val list: ArrayList<CartItem> = ArrayList()

                    for (i in document.documents){

                        val cartItem = i.toObject(CartItem::class.java)!!
                        cartItem.id = i.id

                        list.add(cartItem)
                    }

                    when(activity){
                        is CartListActivity -> {
                            activity.successCartItemsList(list)
                        }
                        is CheckOutActivity -> {
                            activity.successCartItemsList(list)
                        }
                    }

                }.addOnFailureListener {
                    e ->
                    when(activity){
                        is CartListActivity -> {
                            activity.hideProgressDialog()
                        }
                        is CheckOutActivity -> {
                            activity.hideProgressDialog()
                        }
                    }
                    Log.e(activity.javaClass.simpleName,"Terjadi kesalahan selama mendapatkan data item produk keranjang",e)
                }
    }

    fun updateCart(context: Context, cart_id: String, itemHashMap: HashMap<String, Any>){
        mFirestore.collection(Constant.CART_ITEMS)
                .document(cart_id)
                .update(itemHashMap)
                .addOnSuccessListener {

                    when(context){
                        is CartListActivity -> {
                            context.itemUpdateSucces()
                        }
                    }

                }.addOnFailureListener {
                    e->

                    when(context){
                        is CartListActivity -> {
                            context.hideProgressDialog()
                        }
                    }

                    Log.e(
                            context.javaClass.simpleName,"Terjadi kesalahan selama mengupdate item keranjang",e
                    )
                }
    }

    //fungsi untuk memeriksa apakah item sudah atau sebelumnya pernah ada di dalam keranjang atau belum
    fun checkIfItemExistedInCart(activity: ProductDetailsActivity, productId: String){
        mFirestore.collection(Constant.CART_ITEMS) //menambah nilai pada koleksi cart_items
                .whereEqualTo(Constant.USER_ID, getCurrentUserID())
                .whereEqualTo(Constant.PRODUCT_ID, productId)
                .get()
                .addOnSuccessListener { document ->
                    Log.e(activity.javaClass.simpleName, document.documents.toString())
                    if (document.documents.size > 0){
                        activity.productExistedInCart()
                    }else{
                        activity.hideProgressDialog()
                    }
                }
                .addOnFailureListener { e->
                    activity.hideProgressDialog()

                    Log.e(activity.javaClass.simpleName,"Terjadi kesalahan selama memeriksa keranjang",e)
                }

    }

    fun removeItemFromCart(context: Context, cart_id:String){
        mFirestore.collection(Constant.CART_ITEMS)
                .document(cart_id)
                .delete()
                .addOnSuccessListener {
                    when(context){
                        is CartListActivity -> {
                            context.itemRemovedSuccess()
                        }
                    }


                }.addOnFailureListener {
                    //menampilkan pesan kesalahan
                    e ->
                    when(context){
                        is CartListActivity ->{
                            context.hideProgressDialog()
                        }
                    }
                    Log.e(
                            context.javaClass.simpleName,"Terjadi kesalahan saat menghapus item",e
                    )
                }
    }


    fun removeItemFromWishlist(context: Context, wishlist_id:String){
        mFirestore.collection(Constant.WISHLIST_ITEMS)
                .document(wishlist_id)
                .delete()
                .addOnSuccessListener {
                    when(context){
                        is WishlistActivity -> {
                            context.itemRemovedSuccess()
                        }
                    }
                }.addOnFailureListener { e->
                    when(context){
                        is WishlistActivity -> {
                            context.hideProgressDialog()
                        }
                    }
                    Log.e(context.javaClass.simpleName, "Terjadi kesalahan selama menghapus item dari wishlist",e)
                }
    }



        fun getAllProductsList(activity: Activity){

        mFirestore.collection(Constant.PRODUCTS)
                .get()
                .addOnSuccessListener { document ->

                    Log.e("Menampilkan produk",document.documents.toString())

                    val productsList: ArrayList<Product> = ArrayList()

                    for (i in document.documents){

                        val product = i.toObject(Product::class.java)
                        product!!.product_id = i.id

                        productsList.add(product)
                    }

                    when(activity){
                        is CartListActivity ->{
                            activity.successProductsListFromFireStore(productsList)
                        }
                        is CheckOutActivity -> {
                            activity.successProductsListFromFireStore(productsList)
                        }

                        is WishlistActivity -> {
                            activity.successProductsListFromFirestore(productsList)
                        }
                    }


                }.addOnFailureListener { e->
                    when(activity){
                        is CartListActivity ->{
                            activity.hideProgressDialog()
                        }
                        is CheckOutActivity ->{
                            activity.hideProgressDialog()
                        }
                    }
                    Log.e("Get Product List", "Error while getting all product list.", e)
                }
    }


    fun getAddressEstimatedDelivery(fragment: EstimatedDeliveryFragment){
        mFirestore.collection(Constant.ADDRESSES)
                .get()
                .addOnSuccessListener { document ->
                    Log.e(fragment.javaClass.simpleName, document.documents.toString())

                    val estimatedOrdersList: ArrayList<Address> = ArrayList()

                    for (i in document.documents){
                        val estimatedOrders = i.toObject(Address::class.java)!!
                        estimatedOrders.id = i.id
                        estimatedOrdersList.add(estimatedOrders)
                    }
                    fragment.successGetEstimatedDeliveryData(estimatedOrdersList)
                }
    }

    fun getConfirmedOrder(fragment:ConfirmedDeliveryFragment){
        mFirestore.collection(Constant.ORDERS)
                .get()
                .addOnSuccessListener { document ->
                    Log.e(fragment.javaClass.simpleName, document.documents.toString())

                    val confirmedOrderLis: ArrayList<Orders> = ArrayList()

                    for (i in document.documents){
                        val confimedOrders = i.toObject(Orders::class.java)!!
                        confimedOrders.id = i.id
                        confirmedOrderLis.add(confimedOrders)
                    }
                    fragment.successGetConfirmedOrder(confirmedOrderLis)
                }
    }

    fun getDashboardItemList(fragment: HomeFragment){
        mFirestore.collection(Constant.PRODUCTS)
                .get()
                .addOnSuccessListener { document ->
                    Log.e(fragment.javaClass.simpleName, document.documents.toString())

                    val productList: ArrayList<Product> = ArrayList()

                    for (i in document.documents){
                        val product = i.toObject(Product::class.java)!!
                        product.product_id = i.id
                        productList.add(product)
                    }
                    fragment.successDashBoardItemList(productList)
                }
                .addOnFailureListener {
                    e ->
                    //menyembunyikan progress dialog jika ada error selama mendapatkan item list dari dashboard
                    fragment.hideProgressDialog()
                    Log.e(fragment.javaClass.simpleName, "Terjadi kesalahan saat memproses data", e)
                }
    }

    fun addAddress(activity: AddEditAddressActivity, addressInfo: Address){
        mFirestore.collection(Constant.ADDRESSES) // membuat collection baru di firestore
                .document()
                .set(addressInfo, SetOptions.merge())
                .addOnSuccessListener {

                    activity.addUpdateAddressSuccess()

                }.addOnFailureListener {
                    e ->

                    activity.hideProgressDialog()
                    Log.e(
                            activity.javaClass.simpleName,
                            "Terjadi kesalahan selama menambahkan alamat",
                            e
                    )
                }
    }

    fun getAddressesList(activity: AddressListActivity){
        mFirestore.collection(Constant.ADDRESSES)
                .whereEqualTo(Constant.USER_ID, getCurrentUserID())
                .get()
                .addOnSuccessListener {
                    document ->
                    Log.e(activity.javaClass.simpleName, document.documents.toString())

                    val addressList:ArrayList<Address> = ArrayList()

                    for (i in document.documents){
                        val address = i.toObject(Address::class.java)!!
                        address.id = i.id

                        addressList.add(address)
                    }
                    activity.successAddressListFromFireStore(addressList)
                   //membuat fungsi berhasilnya memuat daftar alamat
                }.addOnFailureListener {
                    e->

                    activity.hideProgressDialog()

                    Log.e(activity.javaClass.simpleName, "Terjadi kesalahan selama mendapatkan informasi alamt pengguna",e)
                }
    }

    fun updateAddress(activity: AddEditAddressActivity, addressInfo: Address, addressId: String){
        mFirestore.collection(Constant.ADDRESSES)
                .document(addressId)
                .set(addressInfo, SetOptions.merge())
                .addOnSuccessListener {
                    activity.addUpdateAddressSuccess()
                }.addOnFailureListener {
                    e ->
                    activity.hideProgressDialog()
                    Log.e(
                            activity.javaClass.simpleName,
                            "Terjadi kesalahan selama update",
                            e
                    )
                }
    }

    fun deleteAddress(activity: AddressListActivity, addressId: String){
        mFirestore.collection(Constant.ADDRESSES)
                .document(addressId)
                .delete()
                .addOnSuccessListener {
                    activity.deleteAddressSuccess()
                }.addOnFailureListener {
                    e->
                    activity.hideProgressDialog()
                    Log.e(
                            activity.javaClass.simpleName,
                            "Terjadi kesalahan selagi menghapus alamat",
                            e
                    )
                }

    }

    fun placeOrder(activity: CheckOutActivity, order: Orders){
        mFirestore.collection(Constant.ORDERS)
                .document()
                .set(order, SetOptions.merge())
                .addOnSuccessListener {

                    activity.orderPlacedSuccess()

                }.addOnFailureListener {
                    e ->

                    activity.hideProgressDialog()
                    Log.e(activity.javaClass.simpleName,"Terjadi kesalahan", e)
                }
    }

    fun updateAllDetails(activity: CheckOutActivity, cartList: ArrayList<CartItem>, order: Orders){
        val writeBatch = mFirestore.batch()

        for (cartItem in cartList){

            val soldProduct = SoldProducts(
                FirestoreClass().getCurrentUserID(),
                cartItem.product_name,
                cartItem.product_price,
                cartItem.cart_quantity,
                cartItem.product_image,
                order.product_name,
                order.order_datetime,
                order.sub_total_amount,
                order.shipping_charge,
                order.total_amount,
                order.address
            )

            val documentReference = mFirestore.collection(Constant.SOLD_PRODUCTS)
                    .document(cartItem.product_id)

            writeBatch.set(documentReference, soldProduct)
        } // mengupdate detail stock quantity produk

        for (cartItem in cartList){

            val productHashMap = HashMap<String,Any>()

            productHashMap[Constant.STOCK_QUANTITY] =
                    (cartItem.stock_quantity.toInt() - cartItem.cart_quantity.toInt()).toString()

            val documentReference = mFirestore.collection(Constant.CART_ITEMS)
                    .document(cartItem.id)
            writeBatch.delete(documentReference)
        } //menghapus nilai / isi dalam cart

        writeBatch.commit().addOnSuccessListener {
            activity.allDetailsUpdatedSuccessfully() //memanggil fungsi yang sebelumnya sudah ada di checkout activity
        }.addOnFailureListener {
            e ->

            activity.hideProgressDialog()

            Log.e(activity.javaClass.simpleName, "Terjadi kesalahan saat memberikan perubahan setelah pesanan diproses",e)
        }

    }

    fun getCheckoutList(activity:Activity){
        mFirestore.collection(Constant.SOLD_PRODUCTS)
                .whereEqualTo(Constant.USER_ID, getCurrentUserID())
                .get()
                .addOnSuccessListener { document ->
                    Log.e(activity.javaClass.simpleName, document.documents.toString())

                    val list:ArrayList<Orders> = ArrayList()

                    for (i in document.documents){

                        val orders = i.toObject(Orders::class.java)!!
                        orders.id

                        list.add(orders)
                    }

                when(activity){
                    is PayAtPlaceActivity -> {
                        activity.successGetOrderData(list)
                    }
                }



                }
    }

    fun getMyOrdersList(detailsFragment: OrdersDetailsFragment){
        mFirestore.collection(Constant.ORDERS)
                .whereEqualTo(Constant.USER_ID, getCurrentUserID())
                .get()
                .addOnSuccessListener { document ->
                    Log.e(detailsFragment.javaClass.simpleName, document.documents.toString())
                    val list: ArrayList<Orders> = ArrayList()

                    for (i in document.documents){
                        val orderItem = i.toObject(Orders::class.java)!!
                        orderItem.id = i.id

                        list.add(orderItem)
                    }
                    detailsFragment.populateOrdersListInUI(list)

                }.addOnFailureListener { e ->
                    detailsFragment.hideProgressDialog()
                    Log.e(detailsFragment.javaClass.simpleName, "Terjadi kesalahan selagi mendapatkan daftar pesanan / transaksi",e)
                }
    }

    fun getSoldProductsList(fragment: SoldProductsFragment){
        mFirestore.collection(Constant.SOLD_PRODUCTS)
                .whereEqualTo(Constant.USER_ID, getCurrentUserID())
                .get()
                .addOnSuccessListener { document ->
                    val list: ArrayList<SoldProducts> = ArrayList()
                    for (i in document){
                        val soldProducts = i.toObject(SoldProducts::class.java)
                        soldProducts.id = i.id

                        list.add(soldProducts)
                    }

                    fragment.successSoldProductsList(list)

                }.addOnFailureListener {
                    e ->
                    fragment.hideProgressDialog()
                    Log.e(fragment.javaClass.simpleName,"Terjadi kesalahan selagi mendapatkan daftar produk yang sudah terjual",e)
                }
    }
}
