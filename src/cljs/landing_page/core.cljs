(ns landing-page.core
  (:require [landing-page.app-bar :as app-bar]
            [landing-page.components.modals :as modals]
            [landing-page.context.i18n :as i18n]
            [landing-page.create-account.view :as create-account.view]
            [landing-page.home.view :as home]
            [landing-page.image-list.view :as image-list]
            [landing-page.landing.view :as landing]
            [landing-page.loading :as loading]
            [landing-page.shop.shop-item.view :as shop-item.view]
            [landing-page.shop.view :as shop.view]
            [landing-page.theming.customization-view :as customization-view]
            [landing-page.theming.theme-provider :as theme-provider]
            [landing-page.util :as util]
            [re-frame.core :as rf]
            [reagent-mui.material.box :refer [box]]
            [reagent-mui.material.css-baseline :refer [css-baseline]]
            [reagent-mui.styles :as styles]
            [reagent.core :as r]
            [reagent.dom :as rdom]
            [reitit.frontend :as reitit]
            [reitit.frontend.controllers :as rfc]
            [reitit.frontend.easy :as rfe]))

(rf/reg-event-db
 ::appdb-add-route-name
 (fn [db [_ route-name]]
   (assoc-in db [:route :name] route-name)))

(rf/reg-sub
 ::route-name
 (fn [db [_]]
   (get-in db [:route :name])))

(rf/reg-sub
 ::logged-in?
 :<- [::route-name]
 (fn [route-name]
   (not (contains? #{::index :route/create-account} route-name))))

(defonce match (r/atom nil))

(defn- main-comp []
  [box {:flex 1
        :component "main"}
   (when @match
     (let [view (:view (:data @match))]
       [view @match]))])

(defn- landing-pages-layout []
  [box {:height 1
        :display "flex"
        :flex-direction "column"}
   [:> app-bar/main]
   [main-comp]])

(defn current-page []
  [:<>
   [styles/theme-provider (theme-provider/main)
    [css-baseline {:enable-color-scheme true}]
    [loading/main]
    [modals/modal-comp]
    (if (util/listen [::logged-in?])
      [box {:height 1
            :display "flex"}
       [:> app-bar/main]
       [box {:flex 1}
        [box {:display "flex" :flex-direction "column" :height 1}
         [app-bar/offset {:id "drawer_offset"}]
         [main-comp]]]]
      [landing-pages-layout])]])

(defn mount-root []
  (rdom/render
   [current-page]
   (.getElementById js/document "app")))

(defn log-fn [& params]
  (fn [_]
    (apply js/console.log params)))

(def routes
  (reitit/router
   ["/"
    ["landing"
     {:name ::index
      :view landing/main
      :controllers [{:start (log-fn "start" "landing controller")
                     :stop (log-fn "stop" "landing controller")}]}]
    ["create-account" {:name :route/create-account
                       :view create-account.view/main}]
    ["" {:name :route/home
         :view home/main}]
    ["about-me" {:name :route/about-me
                 :view (fn []
                         [box
                          {:height 1
                           :white-space "pre-line"
                           :width 1
                           :p 2}
                          "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam sollicitudin eros vitae placerat consectetur. Morbi mi augue, tempus vel nisi eget, tempor commodo arcu. Proin eu rutrum ligula. Duis ac enim euismod, convallis est euismod, varius felis. Ut et justo quam. Nullam eu nunc erat. Fusce accumsan magna nec turpis aliquet, sit amet semper dolor convallis. In vel euismod dui, et laoreet dui. Nulla vel erat consequat, laoreet ipsum porttitor, ullamcorper mi. Vestibulum condimentum eros id elit iaculis, gravida dignissim ligula auctor. Proin feugiat magna eu tortor posuere, quis fermentum lacus sagittis. Curabitur non purus nibh. Curabitur congue dui dolor, ut laoreet felis pharetra a. Nulla ut maximus lacus, quis rutrum velit.\n\nCras eget tellus quis ante consectetur tempor non a nibh. Quisque mattis felis et mi congue pellentesque. Sed euismod ullamcorper ultrices. Donec vitae vehicula lectus, vitae accumsan libero. Maecenas laoreet risus quis mi dictum luctus. Aenean enim tellus, lacinia at ligula hendrerit, dapibus sollicitudin lectus. Nunc et placerat nisi. Donec tellus turpis, condimentum eu dui vel, tincidunt tempor magna. Curabitur gravida bibendum urna eget accumsan. Duis ultricies fringilla rutrum. Nunc vel consequat orci.\n\nMorbi euismod purus in lorem rutrum, sit amet semper nibh laoreet. Praesent posuere tincidunt enim a convallis. Duis ut ex vel purus porttitor gravida. Praesent aliquet convallis justo, in tincidunt ligula pharetra quis. Fusce dictum leo quis blandit faucibus. Duis interdum ante pharetra, vulputate neque eu, volutpat turpis. Suspendisse sollicitudin tortor a viverra hendrerit. Nunc lacus velit, dictum vel neque ac, semper congue arcu. Sed quis volutpat leo, eu ornare neque. In hac habitasse platea dictumst. Etiam luctus consectetur felis ac consectetur. Nam mollis est non neque auctor sollicitudin.\n\nNunc accumsan sem ac arcu accumsan convallis. Aliquam augue dui, venenatis a vehicula id, convallis a arcu. Donec ut elit eu dui sollicitudin mattis. Vivamus hendrerit, velit id condimentum molestie, massa urna consectetur justo, auctor imperdiet dolor nisi ut nisi. Nam semper, ante ut finibus feugiat, arcu sapien hendrerit sem, quis viverra diam augue ut purus. Sed venenatis laoreet pharetra. Nam porta tempor ex non viverra. Donec egestas ligula non mi egestas, eget imperdiet lacus fringilla. Praesent euismod ex quis commodo lacinia. Etiam non nulla ullamcorper, tempor leo et, tempus odio. Duis finibus imperdiet mi in facilisis. Vivamus malesuada, purus sit amet ultrices sollicitudin, mauris lorem consectetur eros, et consectetur nibh dui a diam.\n\nNam mattis tempor ante, non commodo quam sagittis ac. In ante elit, hendrerit faucibus ligula at, mattis tristique ex. Nam augue risus, auctor sit amet gravida in, mattis eu risus. Duis fringilla scelerisque tincidunt. Suspendisse ornare ipsum a purus pellentesque, pharetra dictum ante hendrerit. Integer scelerisque quam elit, ac lacinia diam cursus non. Proin diam ligula, facilisis elementum porta a, fermentum a mauris. Morbi dapibus, dolor eu porta venenatis, nunc eros mattis lectus, ut dictum odio sem non massa.\n\nDuis id ultrices felis. Maecenas a orci sed orci suscipit euismod at sed libero. Nulla congue mollis congue. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Maecenas venenatis nunc nec urna eleifend, nec imperdiet libero facilisis. Phasellus et massa volutpat dolor gravida imperdiet. Curabitur tortor lectus, tristique quis massa et, bibendum fermentum erat. Curabitur vitae pretium augue.\n\nProin laoreet ex sit amet tellus dapibus scelerisque. Donec ac diam imperdiet, dignissim purus eu, vestibulum magna. Fusce ac sapien nunc. Suspendisse at aliquam quam. Morbi id mollis neque. Sed ac scelerisque tortor, ut dapibus leo. Vivamus imperdiet lectus quis turpis lobortis hendrerit. Vivamus urna magna, tristique non dui a, cursus congue ipsum. Maecenas lacus justo, viverra ut facilisis et, sollicitudin at ex.\n\nVestibulum elit odio, sodales elementum augue aliquet, facilisis euismod eros. Mauris lectus eros, condimentum non euismod et, efficitur quis nunc. Donec venenatis vulputate urna, vel maximus ex mollis nec. Aliquam hendrerit gravida blandit. Nunc scelerisque auctor lectus sed aliquet. Vestibulum finibus vitae sem nec auctor. Quisque et lectus sed turpis mattis fermentum sed a elit. Vestibulum volutpat, tellus quis auctor facilisis, nunc sapien hendrerit purus, eu hendrerit dolor turpis sit amet felis. Fusce volutpat aliquet dapibus. Nunc eu egestas mi, ut molestie urna. Nullam dignissim, lacus sit amet rutrum auctor, erat lacus dapibus mauris, eget vestibulum nibh libero vel dolor. Praesent sed elit ac quam dictum semper sit amet eu massa. Mauris elit elit, accumsan eget vulputate eu, auctor egestas velit. Integer ut efficitur ipsum. Duis quis nisl quis tortor elementum suscipit. Duis nec augue vel justo interdum venenatis.\n\nQuisque sodales consequat velit, maximus hendrerit lorem cursus a. Duis ac blandit massa. Vestibulum ornare ex eros, sed porta dolor dictum id. Nam nec ante magna. Etiam consequat luctus tempor. Pellentesque ac magna diam. In egestas velit vel lectus pretium cursus.\n\nPraesent nec sapien convallis, imperdiet quam eu, aliquam massa. Etiam elementum aliquet velit in iaculis. Suspendisse lobortis blandit odio ut scelerisque. Donec sit amet orci lacus. Nunc vel dignissim nunc. Pellentesque a mi dui. Phasellus eu neque eu nisi aliquam pretium.\n\nLorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque neque orci, tincidunt eget enim sit amet, lobortis tempus augue. Praesent non nunc ut velit luctus ultrices ac in eros. Donec mi mi, dapibus eget massa nec, posuere mollis ipsum. Sed sit amet mollis ex. Nulla egestas luctus sagittis. Donec nec felis non lectus ullamcorper porta nec gravida lacus. Nam a gravida est. Suspendisse eleifend elit consectetur, tempor ex ut, aliquet dui. Vestibulum est erat, volutpat non libero at, tristique dapibus velit. Integer ultricies, mauris dictum hendrerit consequat, elit mauris consequat dui, nec ullamcorper diam velit at massa. Quisque posuere vulputate lectus ut pulvinar. Pellentesque pharetra erat sapien, eget ultrices augue maximus sodales. Nullam dapibus felis at risus suscipit pretium. Integer a ipsum eget justo lacinia pulvinar. Etiam vitae hendrerit elit, at lobortis nisi.\n\nFusce quis odio nec mauris ornare elementum in at nulla. In pellentesque ex at tincidunt pretium. Curabitur iaculis tellus lacus, et viverra ex volutpat nec. Sed lacinia mattis elit ut fermentum. Nam rhoncus nisi urna. Nunc elementum egestas finibus. Phasellus vitae metus at odio condimentum commodo. Suspendisse ultricies convallis placerat. Integer fringilla imperdiet porta. Suspendisse vel magna nisl. Donec fermentum elit leo, eget feugiat massa commodo vitae. Etiam tincidunt nulla augue, sit amet ornare quam pellentesque ut. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Donec neque magna, porta a augue eget, dapibus egestas odio.\n\nPellentesque vel leo metus. Etiam at turpis metus. Nulla porta augue blandit, pulvinar tortor id, feugiat tortor. Phasellus sodales ultrices elit. Nullam placerat, tellus et varius dapibus, urna nisl egestas ex, non accumsan leo est sed augue. Phasellus id odio id tellus luctus rhoncus. Nulla ac auctor leo, nec venenatis lacus. Vestibulum efficitur imperdiet dignissim. Quisque sit amet mollis arcu. Fusce vel pretium libero.\n\nDuis at justo eget eros pharetra maximus. Nunc at posuere diam. Nullam iaculis mollis feugiat. Donec fringilla nulla vitae felis facilisis, sit amet pretium lorem laoreet. Ut nisi ex, rutrum at condimentum non, pellentesque nec ante. Cras pharetra nulla risus, a venenatis est porttitor ut. Quisque sed cursus nulla, et sagittis urna. Donec feugiat, magna eu tincidunt commodo, purus mi cursus lectus, a convallis nulla felis et risus. Praesent posuere ligula sit amet magna porta, ut pellentesque nisl ultricies. Cras ut ipsum nibh. Morbi quis iaculis dolor, sed sodales dui.\n\nMorbi congue sem sit amet finibus rhoncus. Nunc ac iaculis leo, vitae pharetra ligula. Morbi euismod quis velit non varius. Sed volutpat lacinia vehicula. Sed sit amet massa quis felis porta rutrum. Maecenas elementum lorem nec lorem eleifend, a dignissim neque accumsan. Sed fermentum orci ac erat blandit, in ultricies nisi sollicitudin. Nunc rutrum dui sed velit efficitur pharetra. Fusce volutpat libero sed sapien gravida porttitor. Duis imperdiet egestas velit a iaculis. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Cras consequat scelerisque elit eu tincidunt."])}]
    ["shop" {:name :route/shop
             :view shop.view/main}]
    ["shop-item" {:name :route/shop-item
                  :view shop-item.view/main}]
    ["images" {:name :route/images
               :view image-list/main}]
    ["theme-customization" {:name :route/theme-customization
                            :view customization-view/main}]]))

(defn init! []
  (rfe/start!
   routes
   (fn [new-match]
     (util/>evt [::appdb-add-route-name (get-in new-match [:data :name])])
     (swap! match (fn [old-match]
                    (when new-match
                      (assoc new-match :controllers (rfc/apply-controllers (:controllers old-match) new-match))))))
   {:use-fragment true})
  (i18n/start)
  (mount-root))

(init!)
