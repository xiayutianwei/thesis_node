package thesis.protocol

import thesis.shared.ptcl.ErrorRsp

/**
  * Created by liuziwei on 2018/1/9.
  */
object CommonErrorCode {

  def InternalError(msg:String) = ErrorRsp(100001,msg)
}
