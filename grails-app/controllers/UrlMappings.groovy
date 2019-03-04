class UrlMappings {
//    static excludes = [
//            '/diagram-viewer/*',
//            '/editor-app/*'
//    ]
    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }
        "/dist/api/$apicon/$apiAction"(controller:"base",action:"api")
        "/api/$apicon/$apiAction"(controller:"base",action:"api")
        "/"(controller:"login",action:"login")
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
