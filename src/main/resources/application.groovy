micronaut {
  executors {
    io {
      type = "fixed"
      nThreads = 3
    }
  }
  server {
    port = 8080
  }
}
