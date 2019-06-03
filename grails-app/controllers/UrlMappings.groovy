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
        "/dist/api/$controller/$action?/$id?(.$format)?"{}
        "/api/$controller/$action?/$id?(.$format)?"{}
        "/"(controller:"login",action:"login")
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
