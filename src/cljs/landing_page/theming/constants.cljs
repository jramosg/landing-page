(ns landing-page.theming.constants)


;;; Default values
(def ^:const default-font-size 14)
(def ^:const default-font-family ["Roboto" "Helvetica" "Arial" "sans-serif"])
(def ^:const default-primary-color "#E0C2FF")
(def ^:const default-secondary-color "#cddc39"              ;;lime 500
  )
(def ^:const default-light-offset 70)
(def ^:const default-dark-offset 20)

(def ^:const font-family-opts (conj default-font-family "Fashion Fetish" "Poppins" "monospace"))

(def ^:const theme-root-path [:theme])

(def ^:const typography-path (conj theme-root-path :typography))
(def ^:const font-size-path (conj typography-path :font-size))
(def ^:const font-family-path (conj typography-path :font-family))

(def ^:const palette-path (conj theme-root-path :palette))
(def ^:const primary-color-path (conj typography-path :primary-color))
(def ^:const secondary-color-path (conj typography-path :secondary-color))
(def ^:const light-offset-path (conj palette-path :light-offset))
(def ^:const dark-offset-path (conj palette-path :dark-offset))

