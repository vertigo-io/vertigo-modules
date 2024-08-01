# vertigo-orchestra-ui

A simple VueJS app for the Vertigo's extensions Orchestra
If used in production, you must protect access

## Project setup
```
npm install
```

### Compiles and hot-reloads for development (use port 3000)
```
npm run dev
```
For vertigo-ui, add a `vuiDevMode` attribute on `<vu:page>` tag, it will auto reload js components (use default port only).
Won't work fine with https, and you should inactivate your ContentSecurityPolicy, or add script-src `http://localhost:3000/` connect-src `wss://localhost:3000/`

### Compiles, minifies for production and copy to orchestra resoures folder
```
npm run build-lib
```

### To update libs
`npm outdated`
