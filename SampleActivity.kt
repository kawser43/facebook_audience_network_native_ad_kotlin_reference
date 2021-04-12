import com.facebook.ads.*

class JokesDetailActivity : AppCompatActivity() {

    lateinit private var binding: ActivitySampleBinding
    lateinit private var nativeAdLayout: NativeAdLayout
    lateinit private var adView: LinearLayout
    lateinit private var nativeAd: NativeAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySampleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AudienceNetworkAds.initialize(this)
        loadNativeAd()
    }
    

    fun loadNativeAd() {
        nativeAd = NativeAd(this, "YOUR_PLACEMENT_ID")

        val nativeAdListener: NativeAdListener = object : NativeAdListener{
            override fun onError(p0: Ad?, p1: AdError?) {
                //TODO("Not yet implemented")
            }

            override fun onAdLoaded(ad: Ad?) {
                if (nativeAd == null || nativeAd != ad) {
                    return
                }
                // Inflate Native Ad into Container
                inflateAd(nativeAd!!)
            }

            override fun onAdClicked(p0: Ad?) {
                //TODO("Not yet implemented")
            }

            override fun onLoggingImpression(p0: Ad?) {
                //TODO("Not yet implemented")
            }

            override fun onMediaDownloaded(p0: Ad?) {
                //TODO("Not yet implemented")
            }

        }

        // Request an ad
        nativeAd.loadAd(
            nativeAd.buildLoadAdConfig()
                .withAdListener(nativeAdListener)
                .build())
    }

    private fun inflateAd(nativeAd: NativeAd) {

        nativeAd.unregisterView()

        // Add the Ad view into the ad container.
        nativeAdLayout = binding.nativeAdContainer
        var inflater = LayoutInflater.from(this)
        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
        var adView = inflater.inflate(R.layout.native_ad_layout, nativeAdLayout, false)
        nativeAdLayout.addView(adView)

        // Add the AdOptionsView
        var adChoicesContainer: LinearLayout = findViewById(R.id.ad_choices_container)
        var adOptionsView = AdOptionsView(this, nativeAd, nativeAdLayout)
        adChoicesContainer.removeAllViews()
        adChoicesContainer.addView(adOptionsView, 0)

        // Create native UI using the ad metadata.
        var nativeAdIcon: MediaView = adView.findViewById(R.id.native_ad_icon)
        var nativeAdTitle: TextView = adView.findViewById(R.id.native_ad_title)
        var nativeAdMedia: MediaView = adView.findViewById(R.id.native_ad_media)
        var nativeAdSocialContext: TextView = adView.findViewById(R.id.native_ad_social_context)
        var nativeAdBody: TextView = adView.findViewById(R.id.native_ad_body)
        var sponsoredLabel: TextView = adView.findViewById(R.id.native_ad_sponsored_label)
        var nativeAdCallToAction: Button = adView.findViewById(R.id.native_ad_call_to_action)

        // Set the Text.
        nativeAdTitle.text = nativeAd.getAdvertiserName()
        nativeAdBody.text = nativeAd.getAdBodyText()
        nativeAdSocialContext.text = nativeAd.getAdSocialContext()

        nativeAdCallToAction.visibility = if(nativeAd.hasCallToAction()) View.VISIBLE else View.INVISIBLE
        nativeAdCallToAction.text = nativeAd.getAdCallToAction()
        sponsoredLabel.text = nativeAd.getSponsoredTranslation()

        // Create a list of clickable views
        var clickableViews  = ArrayList<TextView>()
        clickableViews.add(nativeAdTitle)
        clickableViews.add(nativeAdCallToAction)

        // Register the Title and CTA button to listen for clicks.
        nativeAd.registerViewForInteraction(
            adView, nativeAdMedia, nativeAdIcon, clickableViews as List<View>)
    }

}