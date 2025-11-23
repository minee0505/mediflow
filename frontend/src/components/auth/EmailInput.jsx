const EmailInput = () => {
    return (
        <div className="space-y-4">
            <div className="text-sm text-gray-600 bg-sky-50 p-3 rounded-lg border border-sky-200">
                <span className="font-semibold text-sky-700">Step 1:</span> 유효한 이메일을 입력해주세요.
            </div>
            
            <div>
                <label 
                    htmlFor='email'
                    className="block text-sm font-medium text-gray-700 mb-2"
                >
                    이메일 주소
                </label>
                <input
                    id='email'
                    type='email'
                    name='email'
                    required
                    placeholder='example@email.com'
                    className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-sky-500 focus:border-transparent transition-all"
                />
            </div>
            
            <button
                type='button'
                className="w-full py-3 px-4 bg-sky-500 hover:bg-sky-600 text-white rounded-lg font-medium transition-colors duration-200 shadow-md"
            >
                인증 코드 받기
            </button>
        </div>
    );
};

export default EmailInput;