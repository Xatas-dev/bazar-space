package org.bazar.space

import org.bazar.space.application.shared.port.out.Authorizer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import java.util.*

@AutoConfigureMockMvc
class BaseWebTest : BaseIntegrationTest() {
    @Autowired
    lateinit var mockMvc: MockMvc

    @MockitoBean
    lateinit var authorizer: Authorizer

    val authenticatedUserId: UUID = UUID.fromString("fd71e64e-edb2-4ecb-bc90-7f27c34e0af7")
}